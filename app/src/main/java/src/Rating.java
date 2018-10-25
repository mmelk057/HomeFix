package src;

import java.sql.Timestamp;

public class Rating {
    private String comment;
    private String rating;
    private ServiceProvider sp;
    private Timestamp date;

    public Rating(String comment,String rating,ServiceProvider sp){
        this.comment=comment;
        this.rating =rating;
        this.sp = sp;
        date = new Timestamp(System.currentTimeMillis());
    }
    public Rating(String rating,ServiceProvider sp){
        this.rating=rating;
        this.sp=sp;
        this.comment="N/A";
        date = new Timestamp(System.currentTimeMillis());
    }

    public boolean addNewComment(String s){
        return true; //temporary
    }

    public boolean addNewRating(int rating){
        return true; //temporary
    }

    public String getRating(){
        return rating;
    }

    public void setRating(String newRating){
        rating = newRating;
    }

    public String getComment(){
        if (this.comment!=null){
            return comment;
        }
        else{
            return null;
        }
    }

    public void setComment(String newComment){
        comment = newComment;
    }

}
