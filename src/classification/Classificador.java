package classification;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class Classificador {
	
	List<String> featureNames;
	FastVector<Attribute> attrs;
	Classifier classifier;
	final static String posDir = "Data/laptop/";
	final static String negDir = "Data/non_laptop/";
	Instances fullSet;
	ArrayList<Integer> crawleds;
	ArrayList<Integer> rels;
	
	public Classificador(){
		chooseFeaturesToExtract();
		createAttributes();
		rels = new ArrayList<Integer>();
		crawleds = new ArrayList<Integer>();
	}
	
	private void chooseFeaturesToExtract(){
		featureNames = new ArrayList<String>();
		featureNames.add("table");
		featureNames.add("li");
		featureNames.add("td");
		featureNames.add("tr");
		featureNames.add("h3");
		featureNames.add("laptop");
		featureNames.add("specification");
		featureNames.add("tech");
		featureNames.add("notebook");
		featureNames.add("memory");
		featureNames.add("processor");
		featureNames.add("ul");
		featureNames.add("operating");
	}
	
	@SuppressWarnings("deprecation")
	private Attribute addClasses(){
		 FastVector<String> classes = new FastVector<String>(2);
		 classes.add("relevant");
		 classes.add("irrelevant");
		 Attribute classAttribute = new Attribute("class", classes);
		 return classAttribute;
	}
	
	private Attribute addNumericAttribute(String tag){
		Attribute attribute_tag = new Attribute(tag);
		return attribute_tag;
	}
	
	private void createAttributes(){
		attrs = new FastVector<Attribute>(featureNames.size()+1);
		attrs.add(addClasses());
		for(String s : featureNames) attrs.add(addNumericAttribute(s));
	}
	
	protected void createArffFile(){
		ArffSaver saver = new ArffSaver();
		saver.setInstances(fullSet);
	
		try {
			saver.setFile(new File("Data/full_data.arff"));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void createFullSet(){
		fullSet = new Instances("dataset", attrs, 1);
		fullSet.setClassIndex(0);
		
		fullSet.addAll(createPartialSet(posDir));
		fullSet.addAll(createPartialSet(negDir));	
	}
	
	private ArrayList<Instance> createPartialSet(String directory){
		ArrayList<Instance> instanceList = new ArrayList<Instance>();
		
		String[] htmls = new File(directory).list();
		for(String html : htmls){
			Instance instance = extractFeaturesFromDoc(directory+html, true);
			instanceList.add(instance);
		}
		
		return instanceList;
	}
	
	private Document fileToDoc(String filepath){
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(filepath), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private Instance extractFeaturesFromDoc(String filepath, boolean train) {
		Document doc = fileToDoc(filepath);
		
		Instance featureVals = new DenseInstance(featureNames.size()+1);
		featureVals.setDataset(fullSet);
		
		if(train){
			if(filepath.charAt(filepath.length()-6)=='1'){
				featureVals.setValue(0, "relevant");
			} else {
				featureVals.setValue(0, "irrelevant");
			}
		}
				
		Map<String, Integer> dict = tokenizeAndCount(doc);
		
		for(int i = 0; i < featureNames.size(); i++){
			Integer value = new Integer(0);
			if(dict.get(featureNames.get(i))!=null){
				value = dict.get(featureNames.get(i));
			}
			featureVals.setValue(i+1, value);
		}
					
        return featureVals;
	}
	
	private HashMap<String, Integer> tokenizeAndCount(Document doc){
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		
		String[] tokens = doc.html().split(" |\\+|\\@|\\#|\\=|\\%|\\)|\\(|\\:|\\;|\\,|\\.|<|>|\\}|\\{|\\/|[/]");
		
		for(String token : tokens){
			String lowercased = token.toLowerCase();
			if(dict.containsKey(lowercased)){
				Integer curCount = dict.get(lowercased);
				dict.put(lowercased, curCount+1);
			} else dict.put(lowercased, 1);
		}
				
		return dict;
	}

	private boolean classifySingleDoc(String filepath){
        Instance inst = extractFeaturesFromDoc(filepath, false);
        double results[] = new double[2];
		try {
			results = classifier.distributionForInstance(inst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results[0] > results[1];
	}
	
	private ArrayList<String> classifyAllDocsFromSingleDomain(String directory){
		ArrayList<String> relevantDocs = new ArrayList<String>();
		String[] docs = new File(directory).list();
		
		crawleds.add(new Integer(docs.length));
		
		for(String doc : docs)
			if(classifySingleDoc(directory+doc))
				relevantDocs.add(doc);
		
		rels.add(new Integer((int)relevantDocs.size()));
		
		return relevantDocs;
	}
	
	protected ArrayList<ArrayList<String>> classifyAllDocs(String directory){
		ArrayList<ArrayList<String>> relevantDocuments = new ArrayList<>();
		
		String[] foldersPerDomain = new File(directory).list();
		for(String folder : foldersPerDomain){
			relevantDocuments.add(classifyAllDocsFromSingleDomain(directory+folder+"/"));
		}
		
		return relevantDocuments;
	}
	
	protected void loadModel(String filepath){
		try {
			classifier = (RandomForest) SerializationHelper.read(new FileInputStream(filepath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void loadSet(String filepath){
		DataSource source = null;
		try {
			source = new DataSource(filepath);
			fullSet = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fullSet.classIndex() == -1)
		  fullSet.setClassIndex(0);
	}
	
	public ArrayList<Double> calculateDomainsHarvestRatio(){
		ArrayList<Double> results = new ArrayList<>();
		for(int i = 0; i < crawleds.size(); i++){
			Double d = new Double(crawleds.get(i));
			Double n = new Double(rels.get(i));
			results.add(n/d);
		}
		return results;
	}
	
	public double calculateTotalHarvestRatio(){
		double denom = 0, num = 0;
		for(int i = 0; i < crawleds.size(); i++){
			denom += (double) crawleds.get(i);
			num+= (double) rels.get(i);
		}
		
		return num/denom;
	}

    public static void main(String[] args){
    	long start = System.currentTimeMillis();
    	Classificador c = new Classificador(); 
    	c.createFullSet();
    	c.createArffFile();
    	//c.loadSet("Data/full_data.arff");
    	//c.loadModel("Data/random_forest.model");
    	//ArrayList<ArrayList<String>> relevants = c.classifyAllDocs("Data/CrawledHTML/");
    	   
    	long end = System.currentTimeMillis();
    	System.out.println("Time spent (secs): " + ((end-start)/1000.0));
    }          
}