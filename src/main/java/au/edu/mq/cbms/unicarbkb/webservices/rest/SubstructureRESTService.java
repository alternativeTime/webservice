package au.edu.mq.cbms.unicarbkb.webservices.rest;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.log4j.Logger;

import org.eurocarbdb.dataaccess.core.seq.SubstructureQuery;
import org.eurocarbdb.sugar.SugarSequence;

import au.edu.mq.cbms.unicarbkb.webservices.db.Search;
import au.edu.mq.cbms.unicarbkb.webservices.model.BiologicalContext;
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequence;
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequenceToBiologicalContext;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.BiologicalContextResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.BiologicalSource;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.CompleteListResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.IdListResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.GlycanSequenceContent;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.Structure;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.StructureComplete;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.SubStructureRequest;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.TaxonomyResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.TissueTaxonomyResponse;

/**
 * JAX-RS
 * 
 * This class produces a RESTful service to retrieve substructure.
 */
@Path("/substructure/xml")
// substructure
@RequestScoped
public class SubstructureRESTService {
	private static Logger logger = Logger.getLogger("SubstructureRESTService");
	@Inject
	private EntityManager em;

	@POST
	@Path("/id")
	@Produces({ "application/xml" })
	public IdListResponse IdlistSequencesXML(
			SubStructureRequest aSubStructureRequest) {
		IdListResponse aIdList = new IdListResponse();
		List<String> contents = aSubStructureRequest.getSequencects();
		List<Structure> aList = new ArrayList<Structure>();
		if (contents == null) {
			return composeIdList(aIdList, null);
		}

		for (String content : contents) {
			if (!aSubStructureRequest.isReducingEnd()) {
				isnotReduceEnd(aList, content);
			} else {
				isReduceEnd(aList, content);
			}
		}
		return composeIdList(aIdList, aList);
	}

	private IdListResponse composeIdList(IdListResponse aIdList,
			List<Structure> aList) {
		aIdList.setBaseURL("http://www.unicarbkb.org/");
		aIdList.setNumberofStructure(aList.size());
		aIdList.setStructures(aList);
		return aIdList;
	}

	private void isReduceEnd(List<Structure> aList, String content) {
		GlycanSequence result = null;
		Structure aStructure = new Structure();
		try {
			result = (GlycanSequence) em
					.createQuery(
							"select g from GlycanSequence g where g.sequenceCt =?1")
					.setParameter(1, content).getSingleResult(); // content.getContent()
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (result == null)
			aStructure.setId("");
		else
			aStructure.setId((result.getGlycanSequenceId()).toString());
		aList.add(aStructure);
	}

	private void isnotReduceEnd(List<Structure> aList, String aSequence) {
		try {
			List<List<String>> rs = geSequences(aSequence);
			for (int i = 0; i < rs.size(); i++) {
				Structure aStructure = new Structure();
				aStructure.setId(rs.get(i).get(0));
				aList.add(aStructure);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@POST
	@Path("/complete")
	@Produces({ "application/xml" })
	public CompleteListResponse CompletelistSequencesXML(
			SubStructureRequest aSubStructureRequest) {

		return getInformation(aSubStructureRequest);
	}

	private CompleteListResponse getInformation(
			SubStructureRequest aSubStructureRequest) {

		List<StructureComplete> aList = new ArrayList<StructureComplete>();
		CompleteListResponse aIdList = new CompleteListResponse();
		List<String> contents = aSubStructureRequest.getSequencects();
		if (contents == null) {
			return CompleteListResponse.composeCompleteList(aList, null);
		}
		int size = contents.size() > 50 ? 50 : contents.size();
		int i = 0;
		for (String content : contents) {
			++i;
			if (i > size)
				break;
			getCompleteData(aList, content,
					aSubStructureRequest.isReducingEnd());
		}

		return CompleteListResponse.composeCompleteList(aList, aIdList);
	}

	private void getCompleteData(List<StructureComplete> aList, String content,
			boolean isReducingEnd) {
		List<GlycanSequence> results = null;

		if (!isReducingEnd) {
			List<List<String>> rs = geSequences(content);
			if (rs == null) {
				StructureComplete.composeOneStructure(aList, null);
				return;
			}
			for (int i = 0; i < rs.size(); i++) {
				try {
					results = runNoReduceEndSQL(Integer.parseInt(rs.get(i).get(
							0)));
					// System.out.println("rs.get(i).get(0)： " +
					// rs.get(i).get(0));
					StructureComplete.composeOneStructure(aList, results);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			results = runReduceEndSQL(content);
			StructureComplete.composeOneStructure(aList, results);
		}
	}

	@SuppressWarnings("unchecked")
	private List<GlycanSequence> runReduceEndSQL(String sequenceCt) {
		try {
			return em
					.createQuery(
							"select g from GlycanSequence g left outer join g.glycanSequenceToBiologicalContexts gb "
									+ "left outer join gb.biologicalContext b left outer join b.taxonomy left outer join b.tissueTaxonomy where g.sequenceCt =?1")
					.setParameter(1, sequenceCt).getResultList(); // content.getContent()
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<GlycanSequence> runNoReduceEndSQL(int glycanSequenceId) {
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

	List<List<String>> geSequences(String aSequence) {
		try {
			SugarSequence seq = new SugarSequence(aSequence);
			SubstructureQuery query = new SubstructureQuery(seq.getSugar());
			String queryTask = query.getQueryString();
			System.out.println("this is the query string: " + queryTask);
			Search aSearch = new Search();
			return aSearch.getSequencesfromExt(queryTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
	}
}
