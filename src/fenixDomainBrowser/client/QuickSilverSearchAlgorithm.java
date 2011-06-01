package fenixDomainBrowser.client;

public class QuickSilverSearchAlgorithm {
    public static double score(String thiz, String abbreviation, double offset) {

	if (abbreviation.length() == 0) {
	    return 0.9;
	}
	if (abbreviation.length() > thiz.length())
	    return 0.0;

	for (int i = abbreviation.length(); i > 0; i--) {
	    String sub_abbreviation = abbreviation.substring(0, i);
	    int index = thiz.indexOf(sub_abbreviation);

	    if (index < 0)
		continue;
	    if (index + abbreviation.length() > thiz.length() + offset)
		continue;

	    String next_string = thiz.substring(index + sub_abbreviation.length());
	    String next_abbreviation = null;

	    if (i >= abbreviation.length())
		next_abbreviation = "";
	    else
		next_abbreviation = abbreviation.substring(i);

	    double remaining_score = score(next_string, next_abbreviation, offset + index);

	    if (remaining_score > 0) {
		double score = thiz.length() - next_string.length();

		if (index != 0) {
		    int j = 0;

		    int c = thiz.charAt(index - 1);
		    if (c == 32 || c == 9) {
			for (int z = (index - 2); z >= 0; z--) {
			    c = thiz.charAt(z);
			    score -= ((c == 32 || c == 9) ? 1 : 0.15);
			}
			
		          // XXX maybe not port this heuristic
		          // 
		          //          } else if ([[NSCharacterSet uppercaseLetterCharacterSet] characterIsMember:[self characterAtIndex:matchedRange.location]]) {
		          //            for (j = matchedRange.location-1; j >= (int) searchRange.location; j--) {
		          //              if ([[NSCharacterSet uppercaseLetterCharacterSet] characterIsMember:[self characterAtIndex:j]])
		          //                score--;
		          //              else
		          //                score -= 0.15;
		          //            }
		    } else {
			score -= index;
		    }
		}

		score += remaining_score * next_string.length();
		score /= thiz.length();
		return score;
	    }
	}
	return 0.0;
    }
}
