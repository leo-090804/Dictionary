package API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DictionaryAPI {

    public String searchWord(String newWord) {
        // Main output
        StringBuilder output = new StringBuilder();
        try {
            // API REQUESTED TO USE
            URL dictionaryAPI_URL =
                    new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + newWord);

            // CREATE NEW CONNECTION AND SET METHOD
            HttpURLConnection newConnection =
                    (HttpURLConnection) dictionaryAPI_URL.openConnection();

            // HERE WE USING "GET" TO EXPORT DATA
            newConnection.setRequestMethod("GET");

            // READ DATA
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(newConnection.getInputStream()));
            String inputLine;
            StringBuffer wordDetail = new StringBuffer();
            while ((inputLine = input.readLine()) != null) {
                wordDetail.append(inputLine);
            }

            // AFTER GETTING DATA, DISCONNECT
            input.close();
            newConnection.disconnect();

            // JSON HANDLING FROM DATA
            // WITH JSON FILE, WE HAVE AN ARRAY STORING OBJECTS

            // FIRST, WE CONSTRUCT A NEW JSON_ARRAY
            JSONArray wordDetailArray = new JSONArray(wordDetail.toString());

            try {
                if (wordDetailArray.length() <= 0) {
                    throw new NegativeArraySizeException();
                }
            } catch (NegativeArraySizeException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

            //TAKE THE FIRST OBJECT IN THAT ARRAY
            JSONObject wordObject = wordDetailArray.getJSONObject(0);

            // Bien luu tu
            String wordWord = wordObject.optString("word", null);
            String wordPhonetic = wordObject.optString("phonetic", null);
//            System.out.printf("Word: " + wordWord + "\t");
//            System.out.println(wordPhonetic);

            JSONArray audio = wordObject.getJSONArray("phonetics");
            JSONObject audioObject = audio.getJSONObject(0);

            // XÂY DỰNG CHUỖI OUTPUT
            output.append("Word: ").append(wordWord).append("\n");
            output.append(wordPhonetic).append("\n");

            JSONArray meaning = wordObject.getJSONArray("meanings");
            for (int i = 0; i < meaning.length(); i++) {
                JSONObject meaningObject = meaning.getJSONObject(i);
                // Bien luu kieu tu
                String wordPos = meaningObject.optString("partOfSpeech", null);
                //System.out.println("Part of Speecch: " + wordPos);
                output.append("Part of Speech: ").append(wordPos).append("\n");

                JSONArray definitions = meaningObject.getJSONArray("definitions");
                for (int j = 1; j <= definitions.length(); j++) {
                    JSONObject definitionsObject = definitions.getJSONObject(j - 1);
                    // Bien luu dinh nghia
                    String wordDefinition = definitionsObject.optString("definition", null);
                    // Bien luu lai example
                    String wordExample = definitionsObject.optString("example", null);
                    //System.out.println("Definition(" + j + "): " + wordDefinition);
                    output.append("Definition(").append(j).append("): ").append(wordDefinition).append("\n");

                    if (wordExample != null) {
                        //System.out.println("Example: " + wordExample);
                        output.append("Example: ").append(wordExample).append("\n");
                    }
                }
            }

        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
            output.append("Word not found");
        }
        return output.toString();
    }
}