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
		card.setProviderId("VaiCoders1405");
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
			//CardsTermometro();
			//CardsRadar();
			CardsFakeGlassdor();
			CardsFakeLoveMondays();
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
	
	public static void CardsFakeGlassdor() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Glassdoor: Como está o clima lá fora ?";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/Glassdoor_avg.png?raw=true";
		String content =  "Resultado geral de todas as avaliações (média) dos seguintes tópicos:<br><br><ul><li>* Compensation and Benefits</li><li>* Work/Life balance</li><li>* Senior Management</li><li>* Culture & Values</li><li>* Career Opportunities</li></UL>";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}
	
	
	public static void CardsFakeLoveMondays() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Love Mondays: Como está o clima lá fora?";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/Love_mondays_grafico_resumo.PNG?raw=true";
		String content =  "<br><br>Resultado geral de todas as avaliações (média) dos seguintes tópicos:<br><br><ul><li>* Remuneração e benefícios</li><li>* Qualidade de Vida</li><li>* Cultura da Empresa</li><li>* Oportunidade de Carreira</li></UL>";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}
	
	
	public static void CardsAnaliseTendenciaClimaCategoria() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Análise de Tendência do Clima por categoria";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/chart_trend_cat.png?raw=true";
		String content =  "No gráfico podemos ver a análise de tendência de cada uma das categorias do GPTW analisadas.";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}
	
	public static void CardsAnaliseTendenciaClimaLabel() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Análise de Tendência do Clima por label";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/chart_trend_label.png?raw=true";
		String content =  "No gráfico podemos ver a análise de tendência de cada uma das subcategorias do GPTW  (labels) analisadas";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}
	
	
	public static void TagCloudCategory() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Categorias utilizadas";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/tagcloud_category.png?raw=true";
		String content =  "Estas são as categorias mapeadas na análise de clima hoje.";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}
	
	
	public static void TagCloudLabel() {
		CardAPILab cardAPILab = new CardAPILab();		
		Long uniqueID = UniqueID.get();
		String title = "Subcategorias do GPTW  (labels) analisadas";
		String summary = "";
		String url = "https://github.com/erorci/d-coder-lab/blob/master/tagcloud_label.png?raw=true";
		String content =  "Estas são as sub-categorias mapeadas na análise de clima hoje.";
		Attachment attachment = new Attachment();
		attachment.setType("photo");
		attachment.setImageHeight(80L);
		attachment.setImageWidth(80L);
		attachment.setOriginalImageURL(url);		
		cardAPILab.createCard(cardAPILab.createCardObject(uniqueID, title,	summary, content, attachment));		
	}	

}
