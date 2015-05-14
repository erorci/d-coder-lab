package com.ciandt.dcoder.lab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ciandt.dcoder.lab.database.Categoria;
import com.ciandt.dcoder.lab.database.ExtractDataDAO;
import com.ciandt.dcoder.lab.model.Attachment;
import com.ciandt.dcoder.lab.model.Card;
import com.ciandt.dcoder.lab.util.APIUtils;
import com.ciandt.dcoder.lab.util.UniqueID;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class CardAPILab {

	/**
	 * Create a new person inside Smart Canvas
	 */
	public Card createCard(Card card) {

		String apiSpecificPath = "/card/v2/cards";

		Builder builder = APIUtils.createBuilderForPojo(apiSpecificPath);
		ClientResponse response = builder.put(ClientResponse.class, card);

		System.out.println("Create card response:");
		System.out.println(">> Status = " + response.getStatus());
		System.out.println(">> Object = " + response);

		return card;
	}

	/**
	 * Search for a card. This method is better because it returns the numbers
	 * of likes, dislikes and so on.
	 */
	public String searchCards(String query, String localeCode, Long personId) {

		String apiSpecificPath = "/sc2/d-coder/h/brain/card/v3/cards?q="
				+ query;
		if (localeCode != null) {
			apiSpecificPath += "&locale=" + localeCode;
		} else {
			throw new RuntimeException("Locale is required for searches");
		}
		if (personId != null) {
			apiSpecificPath += "&personId=" + personId;
		}

		// Builder builder = APIUtil.createBuilder(apiSpecificPath, queryParam);
		Builder builder = APIUtils.createBuilder("https://d1-prd.appspot.com",
				apiSpecificPath, null);

		// invoke the API
		String response = builder.get(String.class);
		System.out.println("Search cards response = " + response);

		return response;
	}

	/**
	 * Search for a card. This method is better because it returns the numbers
	 * of likes, dislikes and so on.
	 */
	public String getCard(String mnemonic) {

		String apiSpecificPath = "/sc2/d-coder/h/brain/card/v3/cards/"
				+ mnemonic;

		// Builder builder = APIUtil.createBuilder(apiSpecificPath, queryParam);
		Builder builder = APIUtils.createBuilder("https://d1-prd.appspot.com",
				apiSpecificPath, null);

		// invoke the API
		String response = builder.get(String.class);
		System.out.println("Card for mnemonic " + mnemonic + " = " + response);

		return response;
	}

	/**
	 * Create a fake card for testing purposes
	 */
	protected Card createCardObject(Long id, String title, String summary,
			String content, Attachment attachment) {
		Card card = new Card();

		// Basic info
		card.setId(id);
		card.setAuthorDisplayName("VaiCoders");
		card.setAuthorEmail("emersonrs@ciandt.com");
		card.setAuthorId(5639445604728832L);
		card.setAuthorImageURL("https://github.com/erorci/d-coder-lab/blob/master/vaicoders.png?raw=true");
		card.setAutoModerated(true);
		card.setCreatedAt(new Date());
		card.setIsFeatured(false);
		card.setMnemonic("vaicoders-posts");
		card.setProviderUserId("0202198219988251445");
		card.setUpdated(null);
		card.setExpirationDate(null);
		card.setPublishingDate(null);
		card.setSecurityLevel(0);
		card.addCategory("vaicoders");
		card.addCategory("d-coder");

		// i18n
		card.addLanguage("pt");
		card.addLanguage("us");
		card.addRegion("BR");
		card.addRegion("US");

		// Content info
		card.setTitle(title);
		card.setDescription(summary);
		card.setContent(content);
		card.setProviderContentId(String.valueOf(id));
		card.setProviderContentURL("http://ciandt.com");
		card.setProviderId("VaiCodersProviderId1305");
		card.setProviderUpdated(new Date());
		card.setProviderPublished(new Date());

		// Community
		// card.setCommunity("Community Test");
		// card.setCommunityDisplayName("Community Display Name Test");

		card.addAttachment(attachment);

		return card;
	}

	/**
	 * Execute the code
	 * 
	 * @param args
	 *            Command line parameters
	 */
	public static void main(String[] args) {
		try {
			CardsTermometro();
			CardsRadar();
		} catch (Exception exc) {
			exc.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}

	private static void CardsTermometro() {
		CardAPILab cardAPILab = new CardAPILab();
		List<Categoria> cat = new ArrayList<Categoria>();
		ExtractDataDAO dao = new ExtractDataDAO();
		cat = dao.getTermometro();
		String title = "";
		Long uniqueID = 0L;
		String summary = "";
		String content = "";
		String url = "";

		for (Categoria categoria : cat) {
			uniqueID = UniqueID.get();
			title = categoria.getTitulo();
			// summary = "Indicador: " + categoria.getTitulo();
			content = categoria.getDescricao(); // getDescricaoTermometro(categoria.getTitulo());
			url = "http://chart.apis.google.com/chart?chs=425x325&cht=gom&chd=t:"
					+ categoria.getValor().toString()
					+ "&chl="
					+ categoria.getTitulo()
					+ " ["
					+ categoria.getValor().toString() + "]";

			Attachment attachment = new Attachment();
			attachment.setType("photo");
			attachment.setImageHeight(425L);
			attachment.setImageWidth(325L);
			attachment.setOriginalImageURL(url);

			cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,
					summary, content, attachment));
		}
	}

	private static void CardsRadar() {
		CardAPILab cardAPILab = new CardAPILab();
		List<Categoria> cat = new ArrayList<Categoria>();
		ExtractDataDAO dao = new ExtractDataDAO();
		cat = dao.getRadar();
		String title = "";
		Long uniqueID = 0L;
		String summary = "";
		String content = "";
		String url = "";
		String graphicT = "";
		String graphicCHL = "";

		for (Categoria categoria : cat) {
			graphicT += categoria.getValor() + ",";
			graphicCHL += categoria.getTitulo() + "|";
		}

		uniqueID = UniqueID.get();
		title = "Radar por Indicador";
		// summary = "Indicador: " + categoria.getTitulo();
		// content = categoria.getDescricao();
		// //getDescricaoTermometro(categoria.getTitulo());
		url = "http://chart.apis.google.com/chart?cht=p3&chs=450x200&chd=t:"
				+ graphicT.substring(0, graphicT.length() - 1) + "&chl="
				+ graphicCHL.substring(0, graphicCHL.length() - 1)
				+ "&chtt=Indicadores";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(450L);
		attachment.setImageWidth(200L);
		attachment.setOriginalImageURL(url);

		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,
				summary, content, attachment));

	}

}
