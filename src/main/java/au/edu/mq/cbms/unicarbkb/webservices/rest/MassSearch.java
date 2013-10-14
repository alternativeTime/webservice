package au.edu.mq.cbms.unicarbkb.webservices.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import au.edu.mq.cbms.unicarbkb.webservices.db.Search;
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequence;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.CompleteListResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.Mass;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.MsSearchRequest;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.Structure;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.StructureComplete;
import au.edu.mq.cbms.unicarbkb.webservices.ms.MSMatching;

@Path("/peaklist/xml")
//substructure
@RequestScoped
public class MassSearch {
	@Inject
	private EntityManager em;
	private static Logger logger = Logger.getLogger("IdListRESTService");
	private int SCAN_ID = 10000;
	static double screenSize = 1.5;
	@POST
	@Produces({ "application/xml"})
	public CompleteListResponse MassXML(
			MsSearchRequest aMsSearchRequest) {
		List<Mass> massInfo = aMsSearchRequest.getMasses();
		List<StructureComplete> aList = new ArrayList<StructureComplete>();
		CompleteListResponse aIdList = new CompleteListResponse();

	//	massSeachingResult(massInfo);
		getCompleteData(aList, massSeachingResult(massInfo,aMsSearchRequest.getAlgorithm(),aMsSearchRequest.getNumberofresult()));
		return CompleteListResponse.composeCompleteList(aList, aIdList);
	}
	double getMaxIntensity(List<Mass> massInfo){
		double max =0;
		for (Mass aMass:massInfo){
			if (max < aMass.getIntensity())
				max = aMass.getIntensity();
		}
		return max;
	}
	class CustomComparator implements Comparator<Mass> {
	    public int compare(Mass o1, Mass o2) {
	        return ((Double)o1.getMz()).compareTo((Double)o2.getMz());
	    }
	}
	private int massSeachingResult(List<Mass> massInfo, int alg, int number) {
		//if (alg==null) alg =1;
		if (number==0) number=1;
		Search aSearch = new Search();
		MSMatching aMSMatching = new MSMatching();
		double maxIntensity = getMaxIntensity(massInfo);
		Collections.sort(massInfo, new CustomComparator());
		Collections.reverse(massInfo);
		removeOldScanId(aSearch);
		int iCount=0;
		for (Mass aMass:massInfo){

			if (iCount++>=15) break;
			try {
				double a = Math.round(aMass.getMz()/1.5)*100*1.5;
				aSearch.setMSPreference(SCAN_ID, ((int)(Math.round(aMass.getMz()/1.5)*100*1.5)), aMass.getIntensity()/maxIntensity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long result_scan_id = aMSMatching.matchMS(SCAN_ID,alg,number);
		removeOldScanId(aSearch);
		try {
			List<List<String>> idList;
			if((idList= aSearch.getGlycanSequenceID(result_scan_id))!= null){
				return Integer.parseInt(idList.get(0).get(0));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	private void removeOldScanId(Search aSearch) {
		try {
			aSearch.removeMSPreference(SCAN_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getCompleteData(List<StructureComplete> aList, int content) {
		List<GlycanSequence> results = null;
			results = runSQL(content);
			StructureComplete.composeOneStructure(aList, results);
	}
	@SuppressWarnings("unchecked")
	private List<GlycanSequence> runSQL(int glycanSequenceId) {
		try {
			return em
					.createQuery(
							"select g from GlycanSequence g left outer join g.glycanSequenceToBiologicalContexts gb "
									+ "left outer join gb.biologicalContext b left outer join b.taxonomy left outer join b.tissueTaxonomy where g.glycanSequenceId =?1")
					.setParameter(1, glycanSequenceId).getResultList(); // content.getContent()
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
}
