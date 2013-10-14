package au.edu.mq.cbms.unicarbkb.webservices.test;

import static org.junit.Assert.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.*;

public class MassSearchingTest {
    /**
     * Request URLs pulled from system properties in pom.xml
     */
    private static String XML_URL;
    private static String XMLDETAIL_URL;
    private static String JSON_URL;

    /**
     * Property names used to pull values from system properties in pom.xml
     */
   // private static final String XML_PROPERTY = "http://localhost:8080/cross-database-search/rest/substructure/xml";
    private static final String JSON_PROPERTY = "jsonUrl";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "3224";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";

    /**
     * Method executes BEFORE the test method. Values are read from system properties that can be modified in the pom.xml.
     */
    @BeforeClass
    public static void beforeClass() {
    	//test on server 115.146.93.194
    	MassSearchingTest.XMLDETAIL_URL = "http://localhost:10000/cross-database-search/rest/peaklist/xml";//System.getProperty(SubstructureRESTServiceTest.XML_PROPERTY);
    	MassSearchingTest.JSON_URL = System.getProperty(MassSearchingTest.JSON_PROPERTY);
    }

    /**
     * Test method which executes the runRequest method that calls the RESTful helloworld-rs web service.
     */
    @Test
    public void test() {
//        assertEquals("XML Response", SubstructureRESTServiceTest.XML_RESPONSE,
//                this.runRequest(SubstructureRESTServiceTest.XML_URL, MediaType.APPLICATION_XML_TYPE));
        assertEquals("XML Response", MassSearchingTest.XML_RESPONSE,
                this.runRequest2(MassSearchingTest.XMLDETAIL_URL, MediaType.APPLICATION_XML_TYPE));
    }
    public static void main(String [ ] args){

    	MassSearchingTest aMassSearchingTest = new MassSearchingTest();
//    	aMassSearchingTest.readCSV();
    	aMassSearchingTest.runRequest("http://115.146.93.194/cross-database-search/rest/peaklist/xml", MediaType.APPLICATION_XML_TYPE,aMassSearchingTest.readCSV());
//    	aMassSearchingTest.runRequest("http://localhost:10000/cross-database-search/rest/peaklist/xml", MediaType.APPLICATION_XML_TYPE);
    	//    	aMassSearchingTest.runRequest2("http://localhost:10000/cross-database-search/rest/peaklist/xml", MediaType.APPLICATION_XML_TYPE);
//    	aMassSearchingTest.runRequest3("http://localhost:10000/cross-database-search/rest/peaklist/xml", MediaType.APPLICATION_XML_TYPE);
    }
    /**
     * The purpose of this method is to run the external REST request.
     * 
     * @param url The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     * 1497
     */
     String runRequest(String url, MediaType mediaType, List<Mass> aList) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        try {
            // Using the RESTEasy libraries, initiate a client request
            // using the url as a parameter

            MsSearchRequest aMsSearchRequest = new MsSearchRequest();


            //aStructure.setId("");
//            List<Mass> aList = new ArrayList<Mass>();
           // aList.add(aStructure);
           // aStructure = new Structure();
            
            
            aMsSearchRequest.setMasses(aList);
            aMsSearchRequest.setNumberofresult(3);
            for (int i=1; i<=9; i++){
                ClientRequest request = new ClientRequest(url);
                // Be sure to set the mediatype of the request
                request.accept(mediaType);
	            aMsSearchRequest.setAlgorithm(i);
	            request.body(MediaType.APPLICATION_XML_TYPE, aMsSearchRequest);
	            // Request has been made, now let's post the response
	            ClientResponse<CompleteListResponse> response = null;
	            for (int j =0; j<1;j++){
		            long startTime = System.nanoTime();
		            response = request.post(CompleteListResponse.class);
		            List<StructureComplete> structureList = response.getEntity().getStructureComplete();
		            StructureComplete aStructureComplete =  structureList.get(0);
		            result = structureList.get(0).getId();
		            long endTime = System.nanoTime();
		            System.out.println(i + "," + (endTime - startTime) + ":" + result);
	            }
	            // Check the HTTP status of the request
	            // HTTP 200 indicates the request is OK
	            request=null;
	            if (response.getStatus() != 200) {
	                throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
	            }
            }
            // We have a good response, let's now read it
//            List<StructureComplete> structureList = response.getEntity().getStructureComplete();
//            StructureComplete aStructureComplete =  structureList.get(0);
//            result = structureList.get(0).getId();
            // Loop over the br in order to print out the contentsjax
            System.out.println("\n*** Response from Server ***\n");
//            System.out.println(result);
        } catch (ClientProtocolException cpe) {
            System.err.println(cpe);
        } catch (IOException ioe) {
            System.err.println(ioe);
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("\n===============================================");

        return result;
    }
     /**
      * The purpose of this method is to run the external REST request.
      * 
      * @param url The url of the RESTful service
      * @param mediaType The mediatype of the RESTful service
      * 1466
      */
      String runRequest2(String url, MediaType mediaType) {
         String result = null;

         System.out.println("===============================================");
         System.out.println("URL: " + url);
         System.out.println("MediaType: " + mediaType.toString());

         try {
             // Using the RESTEasy libraries, initiate a client request
             // using the url as a parameter
             ClientRequest request = new ClientRequest(url);
             // Be sure to set the mediatype of the request
             request.accept(mediaType);
             MsSearchRequest aMsSearchRequest = new MsSearchRequest();
             Mass aStructure = new Mass();
             //aStructure.setId("");
             List<Mass> aList = new ArrayList<Mass>();
            // aList.add(aStructure);
            // aStructure = new Structure();
             aMsSearchRequest.setAlgorithm(0);
             
             aStructure.setMz(869.3);
             aStructure.setIntensity(22774);
             aList.add(aStructure);
             
             aStructure = new Mass();
             aStructure.setMz(893.3);
             aStructure.setIntensity(17462);
             aList.add(aStructure);
             
             aStructure = new Mass();
             aStructure.setMz(1073.5);
             aStructure.setIntensity(20034);
             aList.add(aStructure);
             
             aStructure = new Mass();
             aStructure.setMz(1193.4);
             aStructure.setIntensity(22793);
             aList.add(aStructure);            
             
             aStructure = new Mass();
             aStructure.setMz(1175.4);
             aStructure.setIntensity(54765);
             aList.add(aStructure);

             aMsSearchRequest.setMasses(aList);
             aMsSearchRequest.setNumberofresult(3);
//             aMsSearchRequest.setMonoisotopic(true);
//             aMsSearchRequest.setPersubstitution(true);
//             aMsSearchRequest.setPpm(true);
             request.body(MediaType.APPLICATION_XML_TYPE, aMsSearchRequest);
             // Request has been made, now let's post the response
             ClientResponse<CompleteListResponse> response = request.post(CompleteListResponse.class);

             // Check the HTTP status of the request
             // HTTP 200 indicates the request is OK
             if (response.getStatus() != 200) {
                 throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
             }

             // We have a good response, let's now read it
             List<StructureComplete> structureList = response.getEntity().getStructureComplete();
             StructureComplete aStructureComplete =  structureList.get(0);
             result = structureList.get(0).getId();
             // Loop over the br in order to print out the contentsjax
             System.out.println("\n*** Response from Server ***\n");
             System.out.println(result);
         } catch (ClientProtocolException cpe) {
             System.err.println(cpe);
         } catch (IOException ioe) {
             System.err.println(ioe);
         } catch (Exception e) {
             System.err.println(e);
         }

         System.out.println("\n===============================================");

         return result;
     }
      /**
       * The purpose of this method is to run the external REST request.
       * 
       * @param url The url of the RESTful service
       * @param mediaType The mediatype of the RESTful service
       */
       String runRequest3(String url, MediaType mediaType) {
          String result = null;

          System.out.println("===============================================");
          System.out.println("URL: " + url);
          System.out.println("MediaType: " + mediaType.toString());

          try {
              // Using the RESTEasy libraries, initiate a client request
              // using the url as a parameter
              ClientRequest request = new ClientRequest(url);
              // Be sure to set the mediatype of the request
              request.accept(mediaType);
              MsSearchRequest aMsSearchRequest = new MsSearchRequest();
              Mass aStructure = new Mass();
              //aStructure.setId("");
              List<Mass> aList = new ArrayList<Mass>();
             // aList.add(aStructure);
             // aStructure = new Structure();
              aMsSearchRequest.setAlgorithm(0);
              aStructure.setMz(1461.5);
              aStructure.setIntensity(1546);
              aList.add(aStructure);
              
              aStructure = new Mass();
              aStructure.setMz(637.7);
              aStructure.setIntensity(2055);
              aList.add(aStructure);
              
              aStructure = new Mass();
              aStructure.setMz(424.1);
              aStructure.setIntensity(2390);
              aList.add(aStructure);
              
              aStructure = new Mass();
              aStructure.setMz(637.2);
              aStructure.setIntensity(3459);
              aList.add(aStructure);            
              
             
              aMsSearchRequest.setMasses(aList);
              aMsSearchRequest.setNumberofresult(3);
//              aMsSearchRequest.setMonoisotopic(true);
//              aMsSearchRequest.setPersubstitution(true);
//              aMsSearchRequest.setPpm(true);
              request.body(MediaType.APPLICATION_XML_TYPE, aMsSearchRequest);
              // Request has been made, now let's post the response
              ClientResponse<CompleteListResponse> response = request.post(CompleteListResponse.class);

              // Check the HTTP status of the request
              // HTTP 200 indicates the request is OK
              if (response.getStatus() != 200) {
                  throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
              }

              // We have a good response, let's now read it
              List<StructureComplete> structureList = response.getEntity().getStructureComplete();
              StructureComplete aStructureComplete =  structureList.get(0);
              result = structureList.get(0).getId();
              // Loop over the br in order to print out the contentsjax
              System.out.println("\n*** Response from Server ***\n");
              System.out.println(result);
          } catch (ClientProtocolException cpe) {
              System.err.println(cpe);
          } catch (IOException ioe) {
              System.err.println(ioe);
          } catch (Exception e) {
              System.err.println(e);
          }

          System.out.println("\n===============================================");

          return result;
      }
       private List<Mass> readCSV(){
    	   String csvFile = "D:/MassSpectrometry/csv/15.csv";
    		BufferedReader br = null;
    		String line = "";
    		String cvsSplitBy = ",";
            List<Mass> aList = new ArrayList<Mass>();
            Mass aStructure = new Mass();

    		try {
    	 
    			br = new BufferedReader(new FileReader(csvFile));
    			while ((line = br.readLine()) != null) {
    	 
    			        // use comma as separator
    				String[] country = line.split(cvsSplitBy);
    	            aStructure.setMz(Double.parseDouble(country[1]));
    	            aStructure.setIntensity(Double.parseDouble(country[2]));
    	            aList.add(aStructure);
    				System.out.println("Country [code= " + country[1] 
    	                                 + " , name=" + country[2] + "]");
    			}
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			if (br != null) {
    				try {
    					br.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		
    		System.out.println("Done");
    		return aList;
    	  }
}
