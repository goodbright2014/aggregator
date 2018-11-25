package io.netty.handler.codec.http.router;

import java.util.ArrayList;
import java.util.List;

public class StingUtilSplit {

	public static String[] splitOnce(String value, char delim) {
        final int delimIndex = value.indexOf(delim);

        // delim wasn't found or it was the last character so return the entire value
        if (delimIndex == -1) {
            return new String[] { value };
        }

        // delim was at the end so just put the first part into the array
        if  (delimIndex == value.length() - 1) {
           return new String[] { value.substring(0, delimIndex) };
        }

        return new String[] { value.substring(0, delimIndex), value.substring(delimIndex + 1) };
    }
	
	private static final String EMPTY_STRING = "";

	/**
	* Splits the specified {@link String} with the specified delimiter.  This operation is a simplified and optimized
	* version of {@link String#split(String)}.
	*/
	public static String[] split(String value, char delim) {
		final int end = value.length();
		final List<String> res = new ArrayList<String>();

		int start = 0;
		for (int i = 0; i < end; i ++) {
			if (value.charAt(i) == delim) {
				if (start == i) {
					res.add(EMPTY_STRING);
				} else {
					res.add(value.substring(start, i));
				}
				start = i + 1;
			}
		}

		if (start == 0) { // If no delimiter was found in the value
			res.add(value);
		} else {
			if (start != end) {
				// Add the last element if it's not empty.
				res.add(value.substring(start, end));
			} else {
				// Truncate trailing empty elements.
				for (int i = res.size() - 1; i >= 0; i --) {
					if (res.get(i).length() == 0) {
						res.remove(i);
					} else {
						break;
					}
				}
			}
		}

		return res.toArray(new String[res.size()]);
	}
}
