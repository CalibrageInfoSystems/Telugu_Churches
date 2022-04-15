package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created model class for API to get bible chapters
 */

public class ChapterReadModel implements Serializable {
    public ChapterReadModel() {
    }

    @SerializedName("Chapter")
    @Expose
    private List<Chapter> chapter = null;

    public List<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(List<Chapter> chapter) {
        this.chapter = chapter;
    }


    public class Verse implements Serializable {
        public Verse() {
        }


        @SerializedName("Verseid")
        @Expose
        private String verseid;
        @SerializedName("Verse")
        @Expose
        private String verse;


        public String getVerseid() {
            return verseid;
        }

        public void setVerseid(String verseid) {
            this.verseid = verseid;
        }

        public String getVerse() {
            return verse;
        }

        public void setVerse(String verse) {
            this.verse = verse;
        }

    }

    public class Chapter implements Serializable {

        public Chapter() {
        }


        @SerializedName("Verse")
        @Expose
        private List<Verse> verse = null;

        public List<Verse> getVerse() {
            return verse;
        }

        public void setVerse(List<Verse> verse) {
            this.verse = verse;
        }

    }
}
