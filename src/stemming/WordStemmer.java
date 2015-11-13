package stemming;

/**
*	NOTE:
*	This class will receive a word and transform the word to lower case
*	allowing for easier evaluation and stemming
*/

public class WordStemmer {
	private static String[] prefixes = {"i", "ka", "mag", "mam", "mang", "man", "ma", 
										"nag", "nam", "nang", "nan", "na",
										"pag", "pam", "pang", "pan", "pa"};
	private static String[] suffixes = {"an", "in"};		//suffixes are only in, an, hin, and han rules of this are explained in the function removeSuffix()
	private static String[] infixes = {"um", "in"};			//infixes are only um and in.

	public static String stemWord(String word) {
		String resultWord, tempWord;
		boolean exit;

		/**
		*	STEP 1: Check Dictionary!
		*/
		//if(RootDictionary.ifRoot(word.toLowerCase())) {
		//	return word;
		//}


		/**
		*	STEP 2: Remove Hyphens
		*/
		resultWord = removeHyphen(word);
		if(!checkConditions(resultWord)) {
			return word;
		} 

		resultWord = resultWord.toLowerCase();

		/**
		*	STEP 3: Remove Infixes.
		*	Doing so will remove confusing infixes between the prefixes
		*/
		exit = false;
		while(!exit) {
			tempWord = removeInfix(resultWord);

			if(checkConditions(tempWord)) {
				if(resultWord.equals(tempWord)) {
					exit = true;
				} else resultWord = tempWord;
			} else {
				return resultWord;
			} 
		}

		/**
		*	STEP 4: Remove Prefixes.
		*/
		exit = false;
		while(!exit) {
			tempWord = removePrefix(resultWord);

			if(checkConditions(tempWord)) {
				if(resultWord.equals(tempWord)) {
					exit = true;
				} else resultWord = tempWord;
			} else {
				return resultWord;
			} 
		}

		/**
		*	STEP 5: Remove Duplicate Syllables.
		*/
		exit = false;
		while(!exit) {
			tempWord = removeDuplicateSyllable(resultWord);

			if(checkConditions(tempWord)) {
				if(resultWord.equals(tempWord)) {
					exit = true;
				} else resultWord = tempWord;
			} else {
				return resultWord;
			}
		}

		/**
		*	STEP 6: Remove Suffixes.
		*/
		exit = false;
		while(!exit) {
			tempWord = removeSuffix(resultWord);

			if(checkConditions(tempWord)) {
				if(resultWord.equals(tempWord)) {
					exit = true;
				} else resultWord = tempWord;
			} else {
				return resultWord;
			}
		}

		/**
		*	STEP 7: ???.
		*/

		return resultWord;
	}

	/**
	*	Checks the Acceptance Conditions.
	*	Words starting with a vowel must have at least 3 letters and at least 1 consonant.
	*	Words starting with a consonant must have at least 4 letters and at least 1 vowel.
	*/
	private static boolean checkConditions(String word) {
		//if(RootDictionary.ifRoot(word)) {
		//	return true;
		//}

		if(word.length() < 3) {
			return false;
		}
		
		if(isVowel(word.charAt(0))) {						//checks is word starts with vowel
			if(word.length() < 3) {							//if word has less than 3 letters then return false
				return false;
			} else {										//word has more than or equal to 3 letters
				for(int i = 0; i < word.length(); i++) {
					if(!isVowel(word.charAt(i))) {			//if word has a consonant, it is valid
						return true;
					}
				}

				return false;								//word has no consonant, it is invalid	
			}
		} else if(word.length() < 4) {						//word starts with consonant and has less than 4 letters
			return false;									//then it is not valid
		} else {											//word has more than or equal to 4 letters
			for(int i = 0; i < word.length(); i++) {		
				if(isVowel(word.charAt(i))) {				//word has a vowel, it is valid
					return true;
				}
			}

			return false;									//word has no vowels, it is not valid
		}
	}

	/**
	*	Assimilation requires a dictionary look-up, but since we don't have a dictionary look up
	*	We'll just do a random selection between indeces. This will cause stemming errors, but
	*	can't really do anything about it
	*/
	private static String assimilateWord(String prefix, String word) {

		if(prefix.endsWith("n")) {
			String[] nResults = new String[4];
			nResults[0] = "d" + word;
			nResults[1] = "l" + word;
			nResults[2] = "s" + word;
			nResults[3] = "t" + word;

			return nResults[Math.round((float)Math.random() * 10) % nResults.length];
		} else if(prefix.endsWith("m")) {
			String[] mResults = new String[2];
			mResults[0] = "b" + word;
			mResults[1] = "p" + word;

			return mResults[Math.round((float)Math.random() * 10) % mResults.length];
		} else if(prefix.endsWith("ng")) {
			String[] ngResults = new String[2];
			ngResults[0] = "k" + word;
			ngResults[1] = word;

			return ngResults[Math.round((float)Math.random() * 10) % ngResults.length];
		}

		return word;
	}

	/**
	*	Finds a prefix in the word and splits the word removing the prefix,
	*	and returns the resulting word.
	*	If no prefix is found, it returns the word parsed
	*/
	private static String removePrefix(String word) {

		for(String prefix: prefixes) {
			if(word.startsWith(prefix)) {
				String result = word.substring(prefix.length());

				/**
				*	If the prefix removed ends with n, m, or ng, it will check assimalatory conditions
				*	and return the resulting word after assimilation
				*/
				if(result.length() >= 3 && isVowel(result.charAt(0)) && (prefix.endsWith("n") || prefix.endsWith("ng") || prefix.endsWith("m"))) {
					result = assimilateWord(prefix, result);
				}
				
				if(checkConditions(result)) {
					return result;
				}
			}
		}

		return word;
	}

	/**
	*	Finds an infix in the word and splits the word removing the infix,
	*	and returns the resulting word.
	*	If no infix is found, it returns the word parsed
	*/
	private static String removeInfix(String word) {

		for(String infix: infixes) {
			if(word.contains(infix)) {
				String[] splitStr = word.split(infix);
				String result = "";

				for(String str: splitStr) {
					result += str;
				}

				/*This way it will only take out -in- that is between the word*/
				/*Take out words starting with um because there are no stemmed words that start with um*/
				if(word.endsWith("in")) {		
					result += "in";
				}

				if(word.startsWith("in")) {
					result = "in" + result;
				}

				if(checkConditions(result)) {
					return result;
				}
			} 
		}

		return word;
	}

	/**
	*	Finds a suffix in the word and splits the word removing the suffix,
	*	and returns the resulting word.
	*	If no suffix is found, it returns the word parsed
	*/
	private static String removeSuffix(String word) {
		String tempWord = word;
				
		for(String suffix: suffixes) {
			/**
			*	If the word ends with a g and has a suffix, and is accepted under the acceptance conditions
			*	Remove the g and reprocess form
			*/
			if(word.endsWith("g") && word.endsWith(suffix + "g") && checkConditions(word.substring(0, word.lastIndexOf('g')))) {
				tempWord = word.substring(0, word.lastIndexOf('g'));
			}

			if(tempWord.endsWith(suffix)) {
				String result = tempWord.substring(0, tempWord.lastIndexOf(suffix.charAt(0)));

				/*
				*	If after trimming the suffix, and an h is found at the end of the resulting word,
				*	remove the h, and return the resulting word
				*
				*	Given that han and hin are only suffixes to words that end in vowels, it is also important
				*	to check the character before the h is a vowel. But since there is no known Tagalog word ending in h 
				*	other than slang words like "noh", "anoh", and "ah", we don't need to check anymore and remove the h regardless.
				*
				*	Also, if the resulting word ends with an u or r, implying the complete suffix is -uin or -rin, 
				*	u must be replaced with o, and r must be replaced with d.
				*	
				*	If the resulting word ends with a consonant and the character before that is u, replace u with o
				*	Example: Punuin will be stemmed to puno (removing in, then replacing u with o)
				*			 Panoorin will be stemmed to panood (removing in, then replacing r with d)
				*			 Lunurin will be stemmed to lunod (removing in, replace the u to o, then replace r with d)
				*/
				char lastChar = result.charAt(result.length() - 1);
				if(result.length() > 2 && result.charAt(result.length() - 2) == 'u' && !isVowel(lastChar)) {
					result = result.substring(0, result.length() - 2) + "o" + lastChar;
				} 

				if(lastChar == 'h') {
					result = result.substring(0, result.length() - 1);
				} else if(lastChar == 'u') {
					result = result.substring(0, result.length() - 1).concat("o"); 
				} else if(lastChar == 'r') {
					result = result.substring(0, result.length() - 1).concat("d"); 
				}

				if(checkConditions(result)) {
					return result;
				}
			}
		}

		return word;
	}

	/**
	*	Removes hyphen and returns only the string after the hyphen.
	*	If no hyphen is found, it will return the word itself.
	*/
	private static String removeHyphen(String word) {
		if(word.contains("-")) {
			return word.substring(word.indexOf("-")+1);
		} else return word;
	}

	/**
	*	Removes duplicate syllables.
	*	If the word starts with a vowel, it will only check if the character next to it is a duplicate.
	*	If the word starts with a consonant, it will check the first and second letters 
	*	with the third and fourth letters respectively if it is a duplicate syllable.
	*/
	private static String removeDuplicateSyllable(String word) {
		String result = word;

		if(isVowel(word.charAt(0))) {
			if(word.charAt(0) == word.charAt(1)) {
				result = word.substring(1);
			}
		} else if(word.charAt(0) == word.charAt(2) && word.charAt(1) == word.charAt(3)) {
			result = word.substring(2);
		} 

		if(checkConditions(result)) {
			return result;
		} else return word;
	}

	/**
	*	Returns true if letter is a vowel
	*/
	private static boolean isVowel(char letter) {
		if(Character.isLetter(letter) && (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u')) {
			return true;
		} else return false;
	}
}