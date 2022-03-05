import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.skyscreamer.jsonassert.JSONAssert;

public class IEMDBTest {

    IEMDB iemdb;

    @Before
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        iemdb = new IEMDB();
        iemdb.handleCommand("addUser {\"email\": \"sara@ut.ac.ir\", \"password\": \"sara1234\", \"name\": \"Sara\", \"nickname\": \"sara\", \"birthDate\": \"1998-03-11\"}");
        iemdb.handleCommand("addUser {\"email\": \"sajjad@ut.ac.ir\", \"password\": \"sajjad1234\", \"name\": \"Sajjad\", \"nickname\": \"sajjad\", \"birthDate\": \"2000-06-14\"}");
        iemdb.handleCommand("addActor {\"id\": 1, \"name\": \"Marlon Brando\", \"birthDate\": \"1924-04-03\", \"nationality\": \"American\"}");
        iemdb.handleCommand("addActor {\"id\": 2, \"name\": \"Al Pacino\", \"birthDate\": \"1940-04-25\", \"nationality\": \"American\"}");
        iemdb.handleCommand("addActor {\"id\": 3, \"name\": \"James Caan\", \"birthDate\": \"1940-03-26\", \"nationality\": \"American\"}");
        iemdb.handleCommand("addMovie {\"id\": 1, \"name\": \"The Godfather\", \"summary\": \"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\", \"releaseDate\": \"1972-03-14\", \"director\": \"Francis Ford Coppola\", \"writers\": [\"Mario Puzo\", \"Francis Ford Coppola\"], \"genres\": [\"Crime\", \"Drama\"], \"cast\": [1, 2, 3], \"imdbRate\": 9.2, \"duration\": 175, \"ageLimit\": 14}");
    }

    @After
    public void teardown() {
        iemdb = null;
    }

    @Test
    public void testRateMovie_happyScenario() {
        String actualResponse = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"score\": 7}");
        String expectedResponse = "{\"success\": true, \"data\": \"movie rated successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":7.0,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRateMovie_wrongMovieId() {
        String actualResponse = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 2, \"score\": 7}");
        String expectedResponse = "{\"success\": false, \"data\": \"MovieNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRateMovie_wrongUserId() {
        String actualResponse = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac\", \"movieId\": 1, \"score\": 7}");
        String expectedResponse = "{\"success\": false, \"data\": \"UserNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRateMovie_userChangeRate() {
        String actualResponse1 = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"score\": 9}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"movie rated successfully\"}";

        String actualResponse2 = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"score\": 7}");
        String expectedResponse2 = "{\"success\": true, \"data\": \"movie rated successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":7.0,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRateMovie_2UsersRate() {
        String actualResponse1 = iemdb.handleCommand("rateMovie {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"score\": 9}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"movie rated successfully\"}";

        String actualResponse2 = iemdb.handleCommand("rateMovie {\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 10}");
        String expectedResponse2 = "{\"success\": true, \"data\": \"movie rated successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":9.5,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_happyScenario() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"comment voted successfully\"}";

        String actualResponse2 = iemdb.handleCommand("voteComment {\"userEmail\": \"sajjad@ut.ac.ir\", \"commentId\": 1, \"vote\": -1}");
        String expectedResponse2 = "{\"success\": true, \"data\": \"comment voted successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[{\"id\":\"1\",\"jsonMap\":{\"like\":1,\"dislike\":1,\"commentId\":1,\"userEmail\":\"sara@ut.ac.ir\",\"text\":\"Interesting Movie\"}}],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_userVotesTwice() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"comment voted successfully\"}";

        String actualResponse2 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": -1}");
        String expectedResponse2 = "{\"success\": true, \"data\": \"comment voted successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[{\"id\":\"1\",\"jsonMap\":{\"like\":0,\"dislike\":1,\"commentId\":1,\"userEmail\":\"sara@ut.ac.ir\",\"text\":\"Interesting Movie\"}}],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_commentNotFound() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 2, \"vote\": 1}");
        String expectedResponse1 = "{\"success\": false, \"data\": \"CommentNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_userNotFound() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara2@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
        String expectedResponse1 = "{\"success\": false, \"data\": \"UserNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_wrongVote() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": 2}");
        String expectedResponse1 = "{\"success\": false, \"data\": \"InvalidVoteValue\"}";

        String actualResponse2 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": -2}");
        String expectedResponse2 = "{\"success\": false, \"data\": \"InvalidVoteValue\"}";
        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVoteComment_voteZero() {
        String actualAddCommentResponse = iemdb.handleCommand("addComment {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1, \"text\": \"Interesting Movie\"}");
        String expectedAddCommentResponse = "{\"success\": true, \"data\": \"comment with id 1 added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("voteComment {\"userEmail\": \"sara@ut.ac.ir\", \"commentId\": 1, \"vote\": 0}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"comment voted successfully\"}";

        String effectActual = iemdb.handleCommand("getMovieById {\"movieId\": 1}");
        String effectExpected = "{\"data\":{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"comments\":[{\"id\":\"1\",\"jsonMap\":{\"like\":0,\"dislike\":0,\"commentId\":1,\"userEmail\":\"sara@ut.ac.ir\",\"text\":\"Interesting Movie\"}}],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"]},\"success\":true}\n";


        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(actualAddCommentResponse, expectedAddCommentResponse, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMoviesByGenre_happyScenario() {
        iemdb.handleCommand("addActor {\"id\": 4, \"name\": \"Adrien Brody\", \"birthDate\": \"1973-04-14\", \"nationality\": \"American\"}");
        iemdb.handleCommand("addActor {\"id\": 5, \"name\": \"Thomas Kretschmann\", \"birthDate\": \"1962-09-08\", \"nationality\": \"German\"}");
        iemdb.handleCommand("addActor {\"id\": 6, \"name\": \"Frank Finlay\", \"birthDate\": \"1926-08-06\", \"nationality\": \"British\"}");
        String actualAddMovieResponse = iemdb.handleCommand("addMovie {\"id\": 2, \"name\": \"The Pianist\", \"summary\": \"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\", \"releaseDate\": \"2002-05-24\", \"director\": \"Roman Polanski\", \"writers\": [\"Ronald Harwood\", \"Wladyslaw Szpilman\"], \"genres\": [\"Biography\", \"Drama\", \"Music\"], \"cast\": [4, 5, 6], \"imdbRate\": 8.5, \"duration\": 150, \"ageLimit\": 12}");
        String expectedAddMovieResponse = "{\"success\": true, \"data\": \"Movie added successfully\"}";

        String actualResponse1 = iemdb.handleCommand("getMoviesByGenre {\"genre\": \"Drama\"}");
        String expectedResponse1 = "{\"data\":[{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"],\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"imdb Rate\":9.2},{\"summary\":\"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\",\"comments\":[],\"releaseDate\":\"2002-05-24\",\"director\":\"Roman Polanski\",\"rating\":null,\"movieId\":2,\"writers\":[\"Ronald Harwood\",\"Wladyslaw Szpilman\"],\"duration\":150,\"cast\":[{\"actorId\":4,\"nationality\":\"American\",\"name\":\"Adrien Brody\",\"birthDate\":\"1973-04-14\"},{\"actorId\":5,\"nationality\":\"German\",\"name\":\"Thomas Kretschmann\",\"birthDate\":\"1962-09-08\"},{\"actorId\":6,\"nationality\":\"British\",\"name\":\"Frank Finlay\",\"birthDate\":\"1926-08-06\"}],\"ageLimit\":12,\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"name\":\"The Pianist\",\"imdb Rate\":8.5}],\"success\":true}\n";

        String actualResponse2 = iemdb.handleCommand("getMoviesByGenre {\"genre\": \"Crime\"}");
        String expectedResponse2 = "{\"data\":[{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"],\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"imdb Rate\":9.2}],\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedAddMovieResponse, actualAddMovieResponse, true);
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMoviesByGenre_noMovieFound() {
        String actualResponse1 = iemdb.handleCommand("getMoviesByGenre {\"genre\": \"Drama2\"}");
        String expectedResponse1 = "{\"data\":[],\"success\":true}";


        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddToWatchList_happyScenario() {
        String actualResponse = iemdb.handleCommand("addToWatchList {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1}");
        String expectedResponse = "{\"success\": true, \"data\": \"movie added to watchlist successfully\"}";

        String effectActual = iemdb.handleCommand("getWatchList {\"userEmail\": \"sara@ut.ac.ir\"}");
        String effectExpected = "{\"data\":{\"WatchList\":[{\"duration\":\"175\",\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"rating\":null,\"movieId\":1,\"imdb Rate\":9.2}]},\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
            JSONAssert.assertEquals(effectActual, effectExpected, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddToWatchList_ageLimitProblem() {
        iemdb.handleCommand("addUser {\"email\": \"saman@ut.ac.ir\", \"password\": \"saman1234\", \"name\": \"Saman\", \"nickname\": \"saman\", \"birthDate\": \"2014-01-01\"}");
        String actualResponse = iemdb.handleCommand("addToWatchList {\"userEmail\": \"saman@ut.ac.ir\", \"movieId\": 1}");
        String expectedResponse = "{\"success\": false, \"data\": \"AgeLimitError\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddToWatchList_wrongMovieId() {
        String actualResponse = iemdb.handleCommand("addToWatchList {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 12}");
        String expectedResponse = "{\"success\": false, \"data\": \"MovieNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddToWatchList_wrongUserId() {
        String actualResponse = iemdb.handleCommand("addToWatchList {\"userEmail\": \"sara1@ut.ac.ir\", \"movieId\": 1}");
        String expectedResponse = "{\"success\": false, \"data\": \"UserNotFound\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, actualResponse, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddToWatchList_movieExists() {
        String actualResponse1 = iemdb.handleCommand("addToWatchList {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1}");
        String expectedResponse1 = "{\"success\": true, \"data\": \"movie added to watchlist successfully\"}";

        String actualResponse2 = iemdb.handleCommand("addToWatchList {\"userEmail\": \"sara@ut.ac.ir\", \"movieId\": 1}");
        String expectedResponse2 = "{\"success\": false, \"data\": \"MovieAlreadyExists\"}";

        try {
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMoviesByDate_happyScenario() {
        iemdb.handleCommand("addActor {\"id\": 4, \"name\": \"Adrien Brody\", \"birthDate\": \"1973-04-14\", \"nationality\": \"American\"}");
        iemdb.handleCommand("addActor {\"id\": 5, \"name\": \"Thomas Kretschmann\", \"birthDate\": \"1962-09-08\", \"nationality\": \"German\"}");
        iemdb.handleCommand("addActor {\"id\": 6, \"name\": \"Frank Finlay\", \"birthDate\": \"1926-08-06\", \"nationality\": \"British\"}");
        String actualAddMovieResponse = iemdb.handleCommand("addMovie {\"id\": 2, \"name\": \"The Pianist\", \"summary\": \"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\", \"releaseDate\": \"2002-05-24\", \"director\": \"Roman Polanski\", \"writers\": [\"Ronald Harwood\", \"Wladyslaw Szpilman\"], \"genres\": [\"Biography\", \"Drama\", \"Music\"], \"cast\": [4, 5, 6], \"imdbRate\": 8.5, \"duration\": 150, \"ageLimit\": 12}");
        String expectedAddMovieResponse = "{\"success\": true, \"data\": \"Movie added successfully\"}";

        String actualResponse1 = iemdb.makeResponse(true, iemdb.getMoviesByDate("2000", "2010"));
        String expectedResponse1 = "{\"data\":[{\"summary\":\"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\",\"comments\":[],\"releaseDate\":\"2002-05-24\",\"director\":\"Roman Polanski\",\"rating\":null,\"movieId\":2,\"writers\":[\"Ronald Harwood\",\"Wladyslaw Szpilman\"],\"duration\":150,\"cast\":[{\"actorId\":4,\"nationality\":\"American\",\"name\":\"Adrien Brody\",\"birthDate\":\"1973-04-14\"},{\"actorId\":5,\"nationality\":\"German\",\"name\":\"Thomas Kretschmann\",\"birthDate\":\"1962-09-08\"},{\"actorId\":6,\"nationality\":\"British\",\"name\":\"Frank Finlay\",\"birthDate\":\"1926-08-06\"}],\"ageLimit\":12,\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"name\":\"The Pianist\",\"imdb Rate\":8.5}],\"success\":true}\n";

        String actualResponse2 = iemdb.makeResponse(true, iemdb.getMoviesByDate("1900", "2010"));
        String expectedResponse2 = "{\"data\":[{\"summary\":\"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\",\"comments\":[],\"releaseDate\":\"1972-03-14\",\"director\":\"Francis Ford Coppola\",\"rating\":null,\"movieId\":1,\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"],\"duration\":175,\"cast\":[{\"actorId\":1,\"nationality\":\"American\",\"name\":\"Marlon Brando\",\"birthDate\":\"1924-04-03\"},{\"actorId\":2,\"nationality\":\"American\",\"name\":\"Al Pacino\",\"birthDate\":\"1940-04-25\"},{\"actorId\":3,\"nationality\":\"American\",\"name\":\"James Caan\",\"birthDate\":\"1940-03-26\"}],\"ageLimit\":14,\"genres\":[\"Crime\",\"Drama\"],\"name\":\"The Godfather\",\"imdb Rate\":9.2},{\"summary\":\"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\",\"comments\":[],\"releaseDate\":\"2002-05-24\",\"director\":\"Roman Polanski\",\"rating\":null,\"movieId\":2,\"writers\":[\"Ronald Harwood\",\"Wladyslaw Szpilman\"],\"duration\":150,\"cast\":[{\"actorId\":4,\"nationality\":\"American\",\"name\":\"Adrien Brody\",\"birthDate\":\"1973-04-14\"},{\"actorId\":5,\"nationality\":\"German\",\"name\":\"Thomas Kretschmann\",\"birthDate\":\"1962-09-08\"},{\"actorId\":6,\"nationality\":\"British\",\"name\":\"Frank Finlay\",\"birthDate\":\"1926-08-06\"}],\"ageLimit\":12,\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"name\":\"The Pianist\",\"imdb Rate\":8.5}],\"success\":true}\n";

        try {
            JSONAssert.assertEquals(expectedAddMovieResponse, actualAddMovieResponse, true);
            JSONAssert.assertEquals(expectedResponse1, actualResponse1, true);
            JSONAssert.assertEquals(expectedResponse2, actualResponse2, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}