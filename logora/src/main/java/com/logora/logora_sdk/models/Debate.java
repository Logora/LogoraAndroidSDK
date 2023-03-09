package com.logora.logora_sdk.models;

import android.util.Log;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Debate {
    private String id;
    private String name;
    private String slug;
    private Date publishedDate;
    private Integer totalVotesCount = 0;
    private Integer usersCount;
    private Integer argumentsCount;
    private List<JSONObject> tagList;
    private List<Position> positionList;
    private String votePosition;
    private HashMap<Integer, Integer> votesCountObject = new HashMap<>();
    private HashMap<Integer, Integer> votesPercentages = new HashMap<>();
    private Integer votePercentage;
    private Integer firstPositionPercentage;
    private Integer secondPositionPercentage;

    public Debate() {}

    public static Debate objectFromJson(JSONObject jsonObject) {
        // Create a new Debate object
        Debate debate = new Debate();
        try {
            // Définir diverses propriétés de l'objet Debate en fonction des valeurs de l'objet JSON
            debate.setName(jsonObject.getString("name"));
            debate.setId(jsonObject.getString("id"));
            debate.setSlug(jsonObject.getString("slug"));
            debate.setUsersCount(jsonObject.getInt("participants_count"));
            debate.setArgumentsCount(jsonObject.getInt("messages_count"));
            String publishedDate = jsonObject.getString("created_at");
            debate.setPublishedDate(DateUtil.parseDate(publishedDate));
            // Extract vote count information from the JSON object and add it to the Debate object
            JSONObject votesCountObject = jsonObject.getJSONObject("votes_count");
            debate.setTotalVotesCount(Integer.parseInt(votesCountObject.getString("total")));
            votesCountObject.remove("total");
            debate.setVotesCountObject(debate.convertToHashMap(votesCountObject));
            // Extraire la position et le pourcentage du vote gagnant de l'objet JSON et l'ajouter à l'objet Debate
            JSONArray debatePositions = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            JSONObject votesCount = jsonObject.getJSONObject("votes_count");
            JSONArray votesCountKeys = votesCount.names();
            int maxPercentage = 0;
            Integer maxId = null;
            int percentage=0;
            int variable=0;
            int resultat=0;
            if(votesCountKeys == null || votesCountKeys.length() == 0) {
                // S'il n'y a pas de votes, définissez le pourcentage de victoires sur 50 % et la position gagnante sur null
                maxPercentage = 50;
                maxId = null;
            } else {
                // Itérer sur le nombre de votes et trouver celui avec le pourcentage le plus élevé
                /*for (int i = 0; i < votesCountKeys.length(); i++) {
                    String key = votesCountKeys.getString(i);
                    if(key.equals("total")) {
                        continue;
                    }
                    int percentage = votesCount.getInt(key);
                    if(percentage > maxPercentage) {
                        maxPercentage = percentage;
                        maxId = Integer.parseInt(key);
                    }
                }*/
                for (int i = 0; i < votesCountKeys.length(); i++) {
                    String key = votesCountKeys.getString(i);
                    String key1= votesCountKeys.getString(votesCountKeys.length()-1);
                    variable = votesCount.getInt(key1) ;
                    System.out.println("voir laaaaaaaaaaaaaaaaaaa"+variable);

                    // System.out.println("voir ici"+key1);

                    System.out.println("keyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"+key);
                    if(key.equals("total")) {
                        continue;
                    }
                    percentage = votesCount.getInt(key) ;
                    resultat=(percentage*100)/variable;
                    System.out.println("resultat est:"+resultat);
                    if(resultat > maxPercentage) {

                        maxPercentage = resultat;
                        System.out.println("maxPercentageeeee"+maxPercentage);
                        maxId = Integer.parseInt(key);
                        System.out.println("maxid"+maxId);
                    }

                }
            }
            debate.setVotePercentage(maxPercentage);
            debate.setVotePosition(getVotePosition(debatePositions, maxId));
            // Extract the positions from the JSON object and add them to the Debate object
            JSONArray positionsObject = jsonObject.getJSONObject("group_context").getJSONArray("positions");
            List<Position> positionsList = new ArrayList<>();
            for (int i=0; i < positionsObject.length(); i++){
                positionsList.add(Position.objectFromJson(positionsObject.getJSONObject(i)));
            }
            debate.setPositionList(positionsList);
            debate.setVotePosition(getVotePosition(positionsList, null));
            debate.initVotePercentage();
            // Return the Debate object
            return debate;
        } catch (JSONException e) {
            // If there is an exception, print the stack trace and return null
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getVotePosition() { return votePosition; }

    public void setVotePosition(String votePosition) { this.votePosition = votePosition; }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public Integer getArgumentsCount() {
        return argumentsCount;
    }

    public void setArgumentsCount(Integer argumentsCount) {
        this.argumentsCount = argumentsCount;
    }

    public Integer getTotalVotesCount() {
        return totalVotesCount;
    }

    public void setTotalVotesCount(Integer totalVotesCount) {
        this.totalVotesCount = totalVotesCount;
    }

    public void incrementTotalVotesCount() { this.totalVotesCount += 1; }

    public void decrementTotalVotesCount() { this.totalVotesCount -= 1; }

    public HashMap<Integer, Integer> getVotesCountObject() {
        return this.votesCountObject;
    }

    public void setVotesCountObject(HashMap<Integer, Integer> votesCountObject) { this.votesCountObject = votesCountObject; }

    public List<JSONObject> getTagList() {
        return tagList;
    }

    public void setTagList(List<JSONObject> tagList) {
        this.tagList = tagList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Integer getVotePercentage() { return votePercentage ; }

    public void setVotePercentage(Integer votePercentage) { this.votePercentage = votePercentage; }

    public Integer getFirstPositionPercentage() { return firstPositionPercentage; }

    public void setFirstPositionPercentage(Integer firstPositionPercentage) { this.firstPositionPercentage = firstPositionPercentage; }

    public Integer getSecondPositionPercentage() { return secondPositionPercentage; }

    public void setSecondPositionPercentage(Integer secondPositionPercentage) { this.secondPositionPercentage = secondPositionPercentage; }

    public int getPositionIndex(Integer index) {
        List<Position> positionList = this.getPositionList();
        Integer positionIndex = null;
        for (int i = 0; i < positionList.size(); i++){
            if(positionList.get(i).getId().equals(index)){
                positionIndex = i;
                return positionIndex;
            }
        }
        return 0;
    }

    public void initVotePercentage() {
        this.setVotesCountObject(votesCountObject);
        List<Integer> votesCountKeys = new ArrayList<>(votesCountObject.keySet());
        Integer maxValue = 50;
        for (int i = 0; i < votesCountKeys.size(); i++) {
            Integer positionId = votesCountKeys.get(i);
            Integer percentage = 100 * votesCountObject.get(positionId) / totalVotesCount;
            if(percentage > maxValue) {
                this.setVotePercentage(percentage);
            }
            this.votesPercentages.put(positionId, percentage);
        }
    }

    public void calculateVotePercentage(String positionId, Boolean isUpdate) {
        //keySet());
        //This line creates a new list of integers called "votesCountKeys" and initializes it with the keys from
        // a Map object called "votesCountObject".
        List<Integer> votesCountKeys = new ArrayList<>(votesCountObject.keySet());
        //This line starts a loop that iterates over each key in "votesCountKeys".
        // The current key is stored in a variable called "key".
        for (int i = 0; i < votesCountKeys.size(); i++) {
            Integer key = votesCountKeys.get(i);
            //This block of code checks if the current key is equal to the value of "positionId".
            // If it is, the value associated with the key in "votesCountObject" is incremented by 1.
            // If it's not, and the "isUpdate"
            // boolean is true, the value associated with the key in
            // "votesCountObject" is decremented by 1
            if(key.equals(Integer.parseInt(positionId))) {
                votesCountObject.put(key, votesCountObject.get(key) + 1);
            } else {
                if (isUpdate) {
                    votesCountObject.put(key, votesCountObject.get(key) - 1);
                }
            }
        }
        this.initVotePercentage();
    }
    
    public Integer getPositionPercentage(Integer positionId) {
        return votesPercentages.get(positionId);
    }

    private static String getVotePosition(JSONArray positions, Integer id) throws JSONException {
        for (int i = 0 ; i < positions.length(); i++) {
            JSONObject position = positions.getJSONObject(i);
            if(i == 0 && id == null) {
                return position.getString("name");
            }
            Integer positionId = position.getInt("id");
            if(positionId.equals(id)) {
                return position.getString("name");
            }
        }
        return "";
    }


    public HashMap<Integer, Integer> convertToHashMap(JSONObject jsonObject) throws JSONException {
        HashMap<Integer, Integer> result = new HashMap<>();
        JSONArray jsonObjectKeys = jsonObject.names();
        for (int i = 0; i < jsonObjectKeys.length(); i++) {
            String key = jsonObjectKeys.getString(i);
            result.put(Integer.parseInt(key), Integer.parseInt(jsonObject.getString(key)));
        }
        return result;
    }

    private static String getVotePosition(List<Position> positions, Integer id) throws JSONException {
        for (int i = 0 ; i < positions.size(); i++) {
            Position position = positions.get(i);
            if(i == 0 && id == null) {
                return position.getName();
            }
            Integer positionId = position.getId();
            if(positionId.equals(id)) {
                return position.getName();
            }
        }
        return "";
    }
}
