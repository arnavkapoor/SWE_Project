public class GetEmoticonFactory {
    public Emoticon getEmote(String EmoteType){
        if(EmoteType == null){
            return null;
        }
        if(EmoteType.equalsIgnoreCase("HAPPY")) {
            Emoticon tempHap = new Happy();
            tempHap.addEmotion();
            return tempHap;
        }
        else if(EmoteType.equalsIgnoreCase("ENVY")){
            Emoticon tempEnvy = new Envy();
            tempEnvy.addEmotion();
            return tempEnvy;
        }
        else if(EmoteType.equalsIgnoreCase("APPRECIATE")) {
            Emoticon tempApp = new Appreciate();
            tempApp.addEmotion();
            return tempApp;
        }
        else if(EmoteType.equalsIgnoreCase("EMBARRASS")) {
            Emoticon tempEmb = new Embarrass();
            tempEmb.addEmotion();
            return tempEmb;
        }
        else if(EmoteType.equalsIgnoreCase("Normal")) {
            Emoticon tempNormal = new Normal();
            tempNormal.addEmotion();
            return tempNormal;
        }

        else if(EmoteType.equalsIgnoreCase("Male")) {
            Emoticon tempMale = new Male();
            tempMale.addEmotion();
            return tempMale;
        }
        return null;
    }
}
