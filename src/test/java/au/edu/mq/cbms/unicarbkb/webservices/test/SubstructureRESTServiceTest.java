package au.edu.mq.cbms.unicarbkb.webservices.test;

import static org.junit.Assert.*;
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

/**
 * JUnit Test class which makes a request to the RESTful of substructure web service.
 * 
 * @author Jingyu ZHANG jingyuzhang2008@gmail.com
 * 
 */
public class SubstructureRESTServiceTest {
    /**
     * Request URLs pulled from system properties in pom.xml
     */
    private static String XML_URL;
    private static String XMLDETAIL_URL;
    private static String JSON_URL;

    /**
     * Property names used to pull values from system properties in pom.xml
     */
    private static final String XML_PROPERTY = "http://localhost:8080/cross-database-search/rest/substructure/xml";
    private static final String JSON_PROPERTY = "jsonUrl";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "28";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";

    /**
     * Method executes BEFORE the test method. Values are read from system properties that can be modified in the pom.xml.
     */
    @BeforeClass
    public static void beforeClass() {
    	//test on server 115.146.93.194
//    	SubstructureRESTServiceTest.XML_URL = "http://115.146.93.194/cross-database-search/rest/substructure/xml/complete";//System.getProperty(SubstructureRESTServiceTest.XML_PROPERTY);
    	SubstructureRESTServiceTest.XML_URL = "http://localhost:10000/cross-database-search/rest/substructure/xml/id";//System.getProperty(SubstructureRESTServiceTest.XML_PROPERTY);
    	SubstructureRESTServiceTest.XMLDETAIL_URL = "http://localhost:10000/cross-database-search/rest/substructure/xml/complete";//System.getProperty(SubstructureRESTServiceTest.XML_PROPERTY);
    	SubstructureRESTServiceTest.JSON_URL = System.getProperty(SubstructureRESTServiceTest.JSON_PROPERTY);
    }

    /**
     * Test method which executes the runRequest method that calls the RESTful helloworld-rs web service.
     */
    @Test
    public void test() {
        assertEquals("XML Response", SubstructureRESTServiceTest.XML_RESPONSE,
                this.runRequest(SubstructureRESTServiceTest.XML_URL, MediaType.APPLICATION_XML_TYPE));
//        assertEquals("XML Response", SubstructureRESTServiceTest.XML_RESPONSE,
//                this.runDetailRequest(SubstructureRESTServiceTest.XMLDETAIL_URL, MediaType.APPLICATION_XML_TYPE));
    }
    public static void main(String [ ] args){

    	SubstructureRESTServiceTest aSubstructureRESTServiceTest = new SubstructureRESTServiceTest();
    	aSubstructureRESTServiceTest.runRequest("http://localhost:8080/cross-database-search/rest/substructure/xml", MediaType.APPLICATION_XML_TYPE);
    }
    /**
     * The purpose of this method is to run the external REST request.
     * 
     * @param url The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
     String runRequest(String url, MediaType mediaType) {
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
            SubStructureRequest aSubStructureRequest = new SubStructureRequest();
            List<String> aList = new ArrayList<String>();
            aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//            aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//            aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//            aSubStructureRequest.setCompleteInformation("ids");
            aSubStructureRequest.setExactMatch(true);
            aSubStructureRequest.setSequencects(aList);
            aSubStructureRequest.setIgnoreReducingAlditol(false);
            aSubStructureRequest.setOthereResiduces(false);
            aSubStructureRequest.setReducingEnd(true);
            aSubStructureRequest.setTerminal(false);
            request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
            // Request has been made, now let's post the response
            ClientResponse<IdListResponse> response = request.post(IdListResponse.class);

            // Check the HTTP status of the request
            // HTTP 200 indicates the request is OK
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
            }

            // We have a good response, let's now read it
            List<Structure> structureList = response.getEntity().getStructures();
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
             SubStructureRequest aSubStructureRequest = new SubStructureRequest();
             List<String> aList = new ArrayList<String>();
             aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//             aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//             aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//             aSubStructureRequest.setCompleteInformation("ids");
             aSubStructureRequest.setExactMatch(true);
             aSubStructureRequest.setSequencects(aList);
             aSubStructureRequest.setIgnoreReducingAlditol(false);
             aSubStructureRequest.setOthereResiduces(false);
             aSubStructureRequest.setReducingEnd(true);
             aSubStructureRequest.setTerminal(false);
             request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
             // Request has been made, now let's post the response
             ClientResponse<IdListResponse> response = request.post(IdListResponse.class);

             // Check the HTTP status of the request
             // HTTP 200 indicates the request is OK
             if (response.getStatus() != 200) {
                 throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
             }

             // We have a good response, let's now read it
             List<Structure> structureList = response.getEntity().getStructures();
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
              SubStructureRequest aSubStructureRequest = new SubStructureRequest();
              List<String> aList = new ArrayList<String>();
              aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//              aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//              aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//              aSubStructureRequest.setCompleteInformation("ids");
              aSubStructureRequest.setExactMatch(true);
              aSubStructureRequest.setSequencects(aList);
              aSubStructureRequest.setIgnoreReducingAlditol(false);
              aSubStructureRequest.setOthereResiduces(false);
              aSubStructureRequest.setReducingEnd(true);
              aSubStructureRequest.setTerminal(false);
              request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
              // Request has been made, now let's post the response
              ClientResponse<IdListResponse> response = request.post(IdListResponse.class);

              // Check the HTTP status of the request
              // HTTP 200 indicates the request is OK
              if (response.getStatus() != 200) {
                  throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
              }

              // We have a good response, let's now read it
              List<Structure> structureList = response.getEntity().getStructures();
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
    private String runDetailRequest(String url, MediaType mediaType) {
        String result = null;

        System.out.println("Detail===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        try {
            // Using the RESTEasy libraries, initiate a client request
            // using the url as a parameter
            ClientRequest request = new ClientRequest(url);

            // Be sure to set the mediatype of the request
            request.accept(mediaType);
            SubStructureRequest aSubStructureRequest = new SubStructureRequest();
            List<String> aList = new ArrayList<String>();
//            GlycanSequenceContent aSequenceContent = new GlycanSequenceContent();
            //empty for biological context
//            aSequenceContent.setContent("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//            aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//            resut 0 
           aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//           no join table
//            aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");

            //            aList.add(aSequenceContent);
//            aSubStructureRequest.setCompleteInformation("ids");
            aSubStructureRequest.setExactMatch(true);
            aSubStructureRequest.setSequencects(aList);
            aSubStructureRequest.setIgnoreReducingAlditol(false);
            aSubStructureRequest.setOthereResiduces(false);
            aSubStructureRequest.setReducingEnd(true);
            aSubStructureRequest.setTerminal(false);
            request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
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
    private String runDetailRequest2(String url, MediaType mediaType) {
        String result = null;

        System.out.println("Detail===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        try {
            // Using the RESTEasy libraries, initiate a client request
            // using the url as a parameter
            ClientRequest request = new ClientRequest(url);

            // Be sure to set the mediatype of the request
            request.accept(mediaType);
            SubStructureRequest aSubStructureRequest = new SubStructureRequest();
            List<String> aList = new ArrayList<String>();
//            GlycanSequenceContent aSequenceContent = new GlycanSequenceContent();
            //empty for biological context
//            aSequenceContent.setContent("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//            aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//            resut 0 
           aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//           no join table
            aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");

            //            aList.add(aSequenceContent);
//            aSubStructureRequest.setCompleteInformation("ids");
            aSubStructureRequest.setExactMatch(true);
            aSubStructureRequest.setSequencects(aList);
            aSubStructureRequest.setIgnoreReducingAlditol(false);
            aSubStructureRequest.setOthereResiduces(false);
            aSubStructureRequest.setReducingEnd(true);
            aSubStructureRequest.setTerminal(false);
            request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
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
    private String runDetailRequest3(String url, MediaType mediaType) {
        String result = null;

        System.out.println("Detail===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        try {
            // Using the RESTEasy libraries, initiate a client request
            // using the url as a parameter
            ClientRequest request = new ClientRequest(url);

            // Be sure to set the mediatype of the request
            request.accept(mediaType);
            SubStructureRequest aSubStructureRequest = new SubStructureRequest();
            List<String> aList = new ArrayList<String>();
//            GlycanSequenceContent aSequenceContent = new GlycanSequenceContent();
            //empty for biological context
//            aSequenceContent.setContent("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//            aList.add("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
//            resut 0 
           aList.add("RES\n1-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");
//           no join table
            aList.add("RES\n1b:b-dglc-HEX-1:5\n2b:b-dglc-HEX-1:5\n3b:b-dglc-HEX-1:5\n4b:b-dglc-HEX-1:5\n5b:b-dglc-HEX-1:5\n6b:b-dglc-HEX-1:5\nLIN\n1:1o(3+1)2d\n2:1o(6+1)3d\n3:3o(6+1)4d\n4:4o(3+1)5d\n5:4o(6+1)6d\n");

            //            aList.add(aSequenceContent);
//            aSubStructureRequest.setCompleteInformation("ids");
            aSubStructureRequest.setExactMatch(true);
            aSubStructureRequest.setSequencects(aList);
            aSubStructureRequest.setIgnoreReducingAlditol(false);
            aSubStructureRequest.setOthereResiduces(false);
            aSubStructureRequest.setReducingEnd(true);
            aSubStructureRequest.setTerminal(false);
            request.body(MediaType.APPLICATION_XML_TYPE, aSubStructureRequest);
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
}

