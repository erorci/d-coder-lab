package com.ciandt.dcoder.lab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ciandt.dcoder.lab.model.Person;
import com.ciandt.dcoder.lab.model.Profile;
import com.ciandt.dcoder.lab.util.APIUtils;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Class that tests Smart Canvas People API
 * @author Daniel Viveiros
 */
public class PeopleAPILab {
	
	/**
	 * Lists all persons created inside Smart Canvas
	 */
	public String listPersons() {
	    
	    //creates the builder
	    String apiSpecificPath = "/people/v2/people";
		Builder builder = APIUtils.createBuilder(apiSpecificPath); 
		
		//invoke the API
        String response = builder.get(String.class);
		System.out.println( "List persons response = " + response);
        
        return response;
	}
	
	/**
     * Search for Smart Canvas persons
     */
    public String searchPersonByEmail(String email) {
        
        String apiSpecificPath = "/people/v2/people/search";
        
        //Parameter
        if ( !StringUtils.isEmpty(email) ) {
            apiSpecificPath += "?email=" + email;
        } 
        
        //creates the builder
        Builder builder = APIUtils.createBuilder(apiSpecificPath);
        
        //invoke the API
        String response = builder.get(String.class);
        System.out.println( "Search person response = " + response);
        
        return response;
    }
    
    /**
     * Create a new person inside Smart Canvas
     * @throws ParseException 
     */
    public Person createPerson() throws ParseException {
        
        String apiSpecificPath = "/people/v2/people";
        Person person = createPersonObject();
        
        Builder builder = APIUtils.createBuilderForPojo(apiSpecificPath);
        ClientResponse response = builder.put(ClientResponse.class, person);

        System.out.println( "Create person response:");
        System.out.println( ">> Status = " + response.getStatus());
        System.out.println( ">> Object = " + response);
        
        return person;
    }
    
    /**
     * Create a new profile inside Smart Canvas
     */
    public Profile createProfile( Person person ) {
        
        String apiSpecificPath = "/people/v2/profiles";
        
        Profile profile = createProfileObject(person);
        
        Builder builder = APIUtils.createBuilderForPojo(apiSpecificPath);
        ClientResponse response = builder.put(ClientResponse.class, profile);

        System.out.println( "Create profile response:");
        System.out.println( ">> Status = " + response.getStatus());
        System.out.println( ">> Object = " + response);
        
        return profile;
    }
    
    /**
     * Lists all profiles inside Smart Canvas
     */
    public void addRoleToProfile( Long profileId, String roleName ) {
        
        Profile profile = new Profile();
        
        //creates the builder
        String apiSpecificPath = "/people/v2/profiles/" + profileId + "/addrole/" + roleName;
        Builder builder = APIUtils.createBuilderForPojo(apiSpecificPath); 
        
        //invoke the API
        ClientResponse response = builder.put(ClientResponse.class, profile);
        System.out.println( "Add role " + roleName + " to profile " +  profileId + " = " + response.getStatus());
    }
    
    /**
     * Lists all profiles by providerid and roles
     */
    public String findProfileByRoleNameAndProviderId( String providerId, String roleName ) {
        //creates the builder
        String apiSpecificPath = "/people/v2/profiles/provider/" + providerId + "/role/" + roleName;
        Builder builder = APIUtils.createBuilder(apiSpecificPath); 
        
        //invoke the API
        String response = builder.get(String.class);
        System.out.println( "Find profiles by provider and role response = " + response);
        
        return response;
    }
    
    /**
     * Creates the person object to be saved inside smart canvas
     * @throws ParseException 
     */
    private Person createPersonObject() throws ParseException {

        Person person = new Person();
        //Make this ID unique, I'm using my phone number here with country and area code
        person.setId( 5511966422351L );
        person.setDisplayName("VaiCoders");
        person.setEmail("emersonr@ciandt.com");
        person.setActive(true);
        person.setBirthdate( new SimpleDateFormat( "dd-MM-yyyy" ).parse("02-02-1982") );
        person.setCompany( "CI&T" );
        person.setGender( "Male" );
        person.setLastUpdate( new Date() );
        person.setLocale( "pt-BR");
        person.setMaritalStatus( "Married" );
        person.setPosition( "Developer" );
        person.setType( "USER" );
        
        return person;
    }
    
    /**
     * Creates the profile object to be saved inside Smart Canvas
     */
    private Profile createProfileObject( Person person ) {
        
        Profile profile = new Profile();
        
        //Make this ID unique, I'll use the person id here
        profile.setId( person.getId() );
        profile.setPersonId( person.getId() );
        profile.setBirthdate( person.getBirthdate() );
        profile.setBraggingRights( "Working to provide smart data");
        profile.setCoach( "cyrillo");
        profile.setCoverURL("https://lh5.googleusercontent.com/-Pk4eXT2yLLQ/VK_YGesg8aI/AAAAAAAAAHI/lHNyOQZShec/w1044-h565-no/sobre.png");
        profile.setDisplayName(person.getDisplayName());
        profile.setEmail(person.getEmail());
        profile.setEmployerName(person.getCompany());
        profile.setGender(person.getGender());
        profile.setImageURL("https://lh3.googleusercontent.com/-OaHRAFfrtY0/VK_X3JDRCjI/AAAAAAAAAG4/FY-2alKz2CA/s129-no/emersonr.jpg");
        profile.setIntroduction("VaiCoders Project");
        profile.setJobTitle(person.getPosition());
        profile.setLastUpdated(person.getLastUpdate());
        profile.setLocale(person.getLocale());
        profile.setManager("aminadab");
        profile.setMaritalStatus(person.getMaritalStatus());
        profile.setPosition(person.getPosition());
        profile.setProfileURL("https://plus.google.com/+EmersonRodrigoOrci");
        
        //You can create your own provider id! That will enable you to search for publishers later
        //using this as a filter, so you will be able to just get the people you want
        profile.setProviderId("EmersonRVaiCoders");
        profile.setProviderUserId("0202198219988251445");
        
        profile.setTagLine("Relevance, Continuously");
        profile.setUsername("emersonr");
        
        return profile;
    }

	/**
	 * Execute the code
	 * @param args Command line parameters
	 */
	public static void main(String[] args) {
		PeopleAPILab peopleAPILab = new PeopleAPILab();
		
		try {
		    
		    //creates the person that will be used later
		    Person person = peopleAPILab.createPerson();
		    Profile profile = peopleAPILab.createProfile(person);
		    peopleAPILab.addRoleToProfile(profile.getId(), "MODERATOR");
		    peopleAPILab.addRoleToProfile(profile.getId(), "PUBLISHER");
		    
		    //this is just for testing
			peopleAPILab.listPersons();
			peopleAPILab.searchPersonByEmail("emersonr@ciandt.com");
			peopleAPILab.findProfileByRoleNameAndProviderId("EmersonRVaiCoders", "PUBLISHER");
		} catch ( Exception exc ) {
			exc.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}

}
