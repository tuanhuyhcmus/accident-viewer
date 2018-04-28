/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.data;

/**
 *
 * @author huynct
 */
public class Session {

    private long session_id;
    private double lat;
    private double lng;
    String first_post_time;
    String end_time;
    String recent_time;
    long history_id;

    public long getSession_id() {
        return session_id;
    }

    public void setSession_id(long session_id) {
        this.session_id = session_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getFirst_post_time() {
        return first_post_time;
    }

    public void setFirst_post_time(String first_post_time) {
        this.first_post_time = first_post_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRecent_time() {
        return recent_time;
    }

    public void setRecent_time(String recent_time) {
        this.recent_time = recent_time;
    }

    public long getHistory_id() {
        return history_id;
    }

    public void setHistory_id(long history_id) {
        this.history_id = history_id;
    }

}
