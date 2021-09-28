package com.logora.logora_sdk.models;

import com.logora.logora_sdk.utils.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Argument extends Model {
    private Integer id;
    private String content;
    private UserBox author;
    private Position position;
    private Integer votesCount;
    private Integer positionIndex;
    private Debate debate;
    private Date publishedDate;
    private List<JSONObject> votesList;
    private Boolean isReply;
    private Integer repliesCount;
    private Integer parentArgumentId = null;
    private String status;
    private List <JSONObject> repliesAuthorsList;

    public Argument() {}

    public static Argument objectFromJson(JSONObject jsonObject) {
        Argument argument = new Argument();
        try {
            argument.setId(jsonObject.getInt("id"));
            argument.setAuthor(UserBox.objectFromJson(jsonObject.getJSONObject("author")));
            argument.setVotesCount(jsonObject.getInt("upvotes"));
            argument.setContent(jsonObject.getString("content"));

            argument.setPosition(Position.objectFromJson(jsonObject.getJSONObject("position")));
            argument.setIsReply(jsonObject.getBoolean("is_reply"));
            argument.setStatus(jsonObject.getString("status"));

            String replyToId = jsonObject.getString("reply_to_id");
            if(!replyToId.equals("null")) {
                argument.setParentArgumentId(Integer.parseInt(replyToId));
            }

            if(jsonObject.has("number_replies")) {
                argument.setRepliesCount(jsonObject.getInt("number_replies"));
            } else {
                argument.setRepliesCount(0);
            }
            argument.setPositionIndex(0);

            if (jsonObject.has("group")) {
                argument.setDebate(Debate.objectFromJson(jsonObject.getJSONObject("group")));
            };
            String publishedDate = jsonObject.getString("created_at");
            argument.setPublishedDate(DateUtil.parseDate(publishedDate));

            JSONArray votesObjects = jsonObject.getJSONArray("votes");
            List<JSONObject> votesList = new ArrayList<>();
            for (int i=0; i < votesObjects.length(); i++){
                votesList.add(votesObjects.getJSONObject(i));
            }
            argument.setVotesList(votesList);

            if (jsonObject.has("replies_authors")) {
                JSONArray repliesAuthorsObjects = jsonObject.getJSONArray("replies_authors");
                List<JSONObject> repliesAuthorsList = new ArrayList<>();
                for (int i = 0; i < repliesAuthorsObjects.length(); i++) {
                    repliesAuthorsList.add(repliesAuthorsObjects.getJSONObject(i));
                }
                argument.setRepliesAuthorsList(repliesAuthorsList);
            }

            return argument;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public UserBox getAuthor() { return author; }

    public void setAuthor(UserBox author) { this.author = author; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Integer getVotesCount() { return votesCount; }

    public void setVotesCount(Integer votesCount) { this.votesCount = votesCount; }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

    public Debate getDebate() { return debate; }

    public void setDebate(Debate debate) { this.debate = debate; }

    public Integer getPositionIndex() { return positionIndex; }

    public void setPositionIndex(Integer positionIndex) { this.positionIndex = positionIndex; }

    public Date getPublishedDate() { return publishedDate ;}

    public void setPublishedDate(Date publishedDate) { this.publishedDate = publishedDate; }

    public List<JSONObject> getVotesList() { return votesList; }

    public void setVotesList(List<JSONObject> votesList) { this.votesList = votesList; }

    public void incrementVotesCount() {
        this.votesCount += 1;
    }

    public void decrementVotesCount() {
        this.votesCount -= 1;
    }

    public boolean getHasUserVoted(Integer userId) {
        for (int i = 0; i < this.votesList.size(); i++){
            try {
                if(this.votesList.get(i).getInt("user_id") == userId){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getUserVoteId(Integer userId) {
        for (int i = 0; i < this.votesList.size(); i++){
            try {
                if(this.votesList.get(i).getInt("user_id") == userId){
                    return votesList.get(i).getInt("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public Boolean getIsReply() {
        return isReply;
    }

    public void setIsReply(Boolean isReply) {
        this.isReply = isReply;
    }

    public Integer getParentArgumentId() {
        return parentArgumentId;
    }

    public void setParentArgumentId(Integer parentArgumentId) {
        this.parentArgumentId = parentArgumentId;
    }

    public Integer getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Integer repliesCount) {
        this.repliesCount = repliesCount;
    }

    public List<JSONObject> getRepliesAuthorsList() { return this.repliesAuthorsList; }

    public void setRepliesAuthorsList(List<JSONObject> repliesAuthorsList) { this.repliesAuthorsList = repliesAuthorsList; }

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }
}