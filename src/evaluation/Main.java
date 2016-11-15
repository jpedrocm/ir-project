package evaluation;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import classification.Classifier;
import extraction.AbstractWrapper;
import extraction.AsusWrapper;
import extraction.BrandSmartUSAWrapper;
import extraction.GeneralExtractor;
import extraction.HPWrapper;
import extraction.JohnLewisWrapper;
import extraction.LenovoWrapper;
import extraction.MicrocenterWrapper;
import extraction.NeweggWrapper;
import extraction.PCWorldWrapper;
import extraction.StaplesWrapper;
import extraction.ToshibaWrapper;
import utils.Utils;

public class Main {    

    private static final AbstractWrapper[] EXTRACTORS = {
            new JohnLewisWrapper(),
            new NeweggWrapper(),
            new AsusWrapper(),
            new MicrocenterWrapper(),
            new ToshibaWrapper(),
            new LenovoWrapper(),
            new BrandSmartUSAWrapper(),
            new StaplesWrapper(),
            new PCWorldWrapper(),
            new HPWrapper()
    };    

    public static void main(String[] args) { 
        EvaluateCrawlerAndExtractor(true);
        EvaluateCrawlerAndExtractor(false);
    }

    private static void EvaluateCrawlerAndExtractor(boolean filtered) {     
        Classifier classifier = new Classifier();        

        System.out.println("Classifying all docs");
        HashMap<String, List<String>> relevantDocs = classifier.classifyAllDocs(Utils.CRAWLED_HTML_PATH.toString(), filtered, Utils.SET_PATH, Utils.MODEL_PATH);
        System.out.println("Docs classified");

        EvaluateCrawler(classifier, filtered);    

        EvaluateExtractor(relevantDocs, filtered);
    }

    private static void EvaluateCrawler(Classifier classifier, boolean filtered) {
        System.out.println("Evaluating Crawler");

        if (filtered)
            System.out.println("filtered");

        System.out.println("Calculating Domain Harvest Ratio");
        HashMap<String, Double> domainHarvestRatio = classifier.calculateDomainsHarvestRatio();
        System.out.println("Calculated");

        System.out.println("Calculating total harvest ratio");
        Double totalHarvestRatio = classifier.calculateTotalHarvestRatio();
        System.out.println("Calculating");

        if (filtered)
            System.out.println("FILTERED CRAWLER RESULTS");
        else
            System.out.println("UNFILTERED CRAWLER RESULTS");

        System.out.println();

        for (String domain : Utils.DOMAIN) {
            if (filtered)
                domain += "Filtered";

            System.out.println("Domain: " + domain);
            System.out.println("Domain harvest ratio: " + domainHarvestRatio.get(domain));
            System.out.println();
        }

        System.out.println("Total harvest ratio: " + totalHarvestRatio);
    }

    private static void EvaluateExtractor(HashMap<String, List<String>> relevantDocs, boolean filtered) {
        System.out.println("Evaluating extractor");
        if (filtered)
            System.out.println("filtered");

        long totalSpecificSpecsColected = 0;
        long totalGeneralSpecsColected = 0;
        long totalCorrectGeneralSpecs = 0;
        
        double totalRecall = 0;
        double totalPrecision = 0; 
        double totalFMeasure = 0;
        
        for (int i = 0; i < Utils.DOMAIN.length; i++) {
            String domain = Utils.DOMAIN[i];

            System.out.println("Domain: " + domain);

            String htmlPath = Paths.get(Utils.CRAWLED_HTML_PATH.toString(), domain).toString();
            if (filtered) {
                htmlPath += "Filtered";
                domain += "Filtered";
            }

            AbstractWrapper generalExtractor = new GeneralExtractor();
            AbstractWrapper specificExtractor = EXTRACTORS[i];

            long specificSpecsColected = 0;
            long generalSpecsColected = 0;
            long correctGeneralSpecs = 0;

            for (String filename : relevantDocs.get(domain)) {
                String filepath = Paths.get(htmlPath, filename).toString();                
                Document doc = Utils.fileToDoc(filepath);

                HashMap<String, List<String>> specificSpecs = specificExtractor.getSpecifications(doc);
                HashMap<String, List<String>> generalSpecs = generalExtractor.getSpecifications(doc);

                
                for (String spec : specificSpecs.keySet())
                    if (generalSpecs.containsKey(spec)) {      
                        boolean allEqual = true;
                        for (String specValue : specificSpecs.get(spec))
                            if (!generalSpecs.get(spec).contains(specValue)) {
                                allEqual = false;
                                break;
                            }

                        if (allEqual) {
                            correctGeneralSpecs++;
                            totalCorrectGeneralSpecs++;
                        }                        
                    }

                specificSpecsColected += specificSpecs.size();
                totalSpecificSpecsColected += specificSpecs.size();

                generalSpecsColected += generalSpecs.size();
                totalGeneralSpecsColected += generalSpecs.size();

            }

            double recall = (double) correctGeneralSpecs / specificSpecsColected;
            if (Double.isFinite(recall))
                totalRecall += recall;

            double precision = (double) correctGeneralSpecs / generalSpecsColected;
            if (Double.isFinite(precision))
                totalPrecision += precision;

            double fMeasure = (2 * recall * precision) / (recall + precision);
            if (Double.isFinite(fMeasure))
                totalFMeasure += fMeasure;

            if (filtered)
                System.out.println("FILTERED EXTRACTION RESULTS");
            else
                System.out.println("EXTRACTION RESULTS");

            System.out.println("Domain: " + domain);
            System.out.println();
            System.out.println("Total specific extractions: " + specificSpecsColected);
            System.out.println("Total general extractions: " + generalSpecsColected);
            System.out.println("Correct general extractions: " + correctGeneralSpecs);
            System.out.println();
            System.out.println("Recall: " + recall);
            System.out.println("Precision: " + precision);
            System.out.println("F-Measure: " + fMeasure);
            System.out.println();
        }


        double recall = (double) totalCorrectGeneralSpecs / totalSpecificSpecsColected;

        double precision = (double) totalCorrectGeneralSpecs / totalGeneralSpecsColected;

        double fMeasure = (2 * recall * precision) / (recall + precision);

        System.out.println("GLOBAL RESULTS");
        System.out.println();
        System.out.println("Total specific extractions: " + totalSpecificSpecsColected);
        System.out.println("Total general extractions: " + totalGeneralSpecsColected);
        System.out.println("Correct general extractions: " + totalCorrectGeneralSpecs);
        System.out.println();
        System.out.println("Micro Evaluation");
        System.out.println("Recall: " + recall);
        System.out.println("Precision: " + precision);
        System.out.println("F-Measure: " + fMeasure);
        System.out.println();
        System.out.println("Macro Evaluation");
        System.out.println("Recall: " + totalRecall / Utils.DOMAIN.length);
        System.out.println("Precision: " + totalPrecision / Utils.DOMAIN.length);
        System.out.println("F-Measure: " + totalFMeasure / Utils.DOMAIN.length);
        System.out.println();
    }   

}