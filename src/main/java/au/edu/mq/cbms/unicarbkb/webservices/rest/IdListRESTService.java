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
import au.edu.mq.cbms.unicarbkb.webservices.model.GlycanSequence;

import au.edu.mq.cbms.unicarbkb.webservices.model.ws.CompleteListResponse;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.IdListRequest;

import au.edu.mq.cbms.unicarbkb.webservices.model.ws.Structure;
import au.edu.mq.cbms.unicarbkb.webservices.model.ws.StructureComplete;

/**
 * IdListRESTService
 * 
 * This class produces a RESTful service to retrieve substructure.
 */
@Path("/idList/xml")
//substructure
@RequestScoped
public class IdListRESTService {
	@Inject
	private EntityManager em;
	private static Logger logger = Logger.getLogger("IdListRESTService");
	@POST
	@Produces({ "application/xml"})
	public CompleteListResponse IdlistXML(
			IdListRequest aIdListRequest) {
		List<Structure> contents = aIdListRequest.getStructures();
		contents.get(0).getId();
		List<StructureComplete> aList = new ArrayList<StructureComplete>();
		CompleteListResponse aIdList = new CompleteListResponse();

		int size = contents.size() > 50 ? 50 : contents.size();
		int i = 0;
		for (Structure content : contents) {
			++i;
			if (i > size)
				break;
			getCompleteData(aList, content.getId()
					);
		}

		return CompleteListResponse.composeCompleteList(aList, aIdList);
	}
	private void getCompleteData(List<StructureComplete> aList, String content) {
		List<GlycanSequence> results = null;
			results = runSQL(content);
			StructureComplete.composeOneStructure(aList, results);
	}
	@SuppressWarnings("unchecked")
	private List<GlycanSequence> runSQL(String glycanSequenceId) {
		try {
			return em
					.createQuery(
							"select g from GlycanSequence g left outer join g.glycanSequenceToBiologicalContexts gb "
									+ "left outer join gb.biologicalContext b left outer join b.taxonomy left outer join b.tissueTaxonomy where g.glycanSequenceId =?1")
					.setParameter(1, Integer.parseInt(glycanSequenceId)).getResultList(); // content.getContent()
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
}
