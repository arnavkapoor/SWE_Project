

/**
 *  Class that holds all bowler info
 *
 */

public class Bowler {

    final private String fullName;
    final private String nickName;
    final private String email;

    public Bowler( String nick, String full, String mail ) {
	nickName = nick;
	fullName = full;
  	email = mail;
    }


    public String getNickName() {

        return nickName;  

    }

	public String getFullName ( ) {
			return fullName;
	}
	
	public String getNick ( ) {
		return nickName;
	}

	public String getEmail ( ) {
		return email;	
	}
	
	public boolean equals ( Bowler b) {
		boolean retval = true;
		if ( !(nickName.equals(b.getNickName())) ) {
				retval = false;
		}
		if ( !(fullName.equals(b.getFullName())) ) {
				retval = false;
		}	
		if ( !(email.equals(b.getEmail())) ) {
				retval = false;
		}
		return retval;
	}
}