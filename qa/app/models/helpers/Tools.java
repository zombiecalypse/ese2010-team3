package models.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import models.SearchEngine.StopWords;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.pegdown.PegDownProcessor;

public class Tools {

	/**
	 * Digest a password with the SHA-1 hash algorithm.
	 * 
	 * @param password
	 * @return the fingerprint of the password
	 */
	public static String encrypt(String password) {
		return digest(password, "SHA-1");
	}

	/**
	 * Calculates a digital fingerprint for a given string with a given hash
	 * algorithm such as MD5 or SHA-1.
	 * 
	 * @param string
	 *            the string to calculate the fingerprint for
	 * @param hashAlgorithm
	 *            the hash algorithm to be used (e.g. MD5 or SHA-1)
	 * @return a hex digest of at most 32 chars for MD5 or 40 chars for SHA-1
	 *         (or <code>null</code> if the desired algorithm isn't available)
	 */
	public static String digest(String string, String hashAlgorithm) {
		try {
			MessageDigest m = MessageDigest.getInstance(hashAlgorithm);
			return new BigInteger(1, m.digest(string.getBytes()))
					.toString(16);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Check an email-address to be valid.
	 * 
	 * @param email
	 * @return true if the email is valid.
	 */
	public static boolean checkEmail(String email) {
		return email.matches("\\S+@(?:[A-Za-z0-9-]+\\.)+\\w{2,4}");
	}

	public static final String DATE_FORMAT_CH = "dd.MM.yyyy";
	public static final String DATE_FORMAT_US = "MM/dd/yyyy";
	public static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	/**
	 * Turns the Date object d into a String using the format given in the
	 * constant DATE_FORMAT.
	 */
	public static String dateToString(Date d) {
		if (d != null)
			return new SimpleDateFormat(DATE_FORMAT_CH).format(d);
		return null;
	}

	/**
	 * Turns the String object s into a Date assuming the format given in the
	 * constant DATE_FORMAT.
	 * 
	 * @throws ParseException
	 */
	public static Date stringToDate(String s) throws ParseException {
		if (s.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}"))
			return new SimpleDateFormat(DATE_FORMAT_CH).parse(s);
		if (s.matches("\\d{1,2}/\\d{1,2}/\\d{4}"))
			return new SimpleDateFormat(DATE_FORMAT_US).parse(s);
		if (s.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
			return new SimpleDateFormat(DATE_FORMAT_ISO).parse(s);
		return null;
	}

	/**
	 * Takes a String of words with at least 4 characters and counts the
	 * occurrence. Words that occur more than once are treated as important
	 * words.
	 * 
	 * @param input
	 *            with all the words that contain more than 3 characters
	 * @return keywords with words that occur more than 3 times
	 */
	public static String extractImportantWords(String input) {
		HashMap<String, Integer> keywords = new HashMap();
		for (String word : input.toLowerCase().split("\\s+")) {
			if (word.length() <= 3)
				continue;
			Integer count = keywords.get(word);
			if (count == null)
				count = 0;
			keywords.put(word, count + 1);
		}

		HashMap<String, Integer> filtered = new HashMap();
		for (String word : keywords.keySet()) {
			Integer count = keywords.get(word);
			if (count > 1 && !StopWords.get().contains(word))
				filtered.put(word, -count);
		}

		List<String> sorted = Mapper.sortByValue(filtered);
		if (sorted.size() > 5)
			sorted = sorted.subList(0, 5);
		Collections.sort(sorted);
		return fromStringList(sorted, " ");
	}

	/**
	 * Joins all strings from a list with a given joiner.
	 * 
	 * @param list
	 *            a list of strings
	 * @param joiner
	 *            a string to be inserted between to strings to join them
	 * @return the resulting string
	 */
	public static String fromStringList(List<String> list, String joiner) {
		String result = "";
		for (String string : list)
			result += joiner + string;
		if (result.length() > 0)
			result = result.substring(joiner.length());
		return result;
	}

	/**
	 * Sorts a list of Questions and segments them into parts according to a
	 * certain number of entries per part.
	 * 
	 * @param entries
	 *            the list to be segmented
	 * @param entriesPerPage
	 *            the amount of entries on one page
	 * @param pageNumber
	 *            the number of the requested page
	 * 
	 * 
	 * @return a list of the entries on the given page number.
	 * 
	 */
	public static <A> List<A> paginate(List<A> entries,
			int entriesPerPage, int index) {
		int limit = entries.size();
		int upperBound = ((index + 1) * entriesPerPage);

		if (upperBound <= limit)
			return entries.subList(index * entriesPerPage, upperBound);
		if (index * entriesPerPage <= limit)
			return entries.subList(index * entriesPerPage, limit);

		return new ArrayList<Question>();
	}

	public static int determineMaximumIndex(List<?> list,
			int entriesPerPage) {
		return (list.size() - 1) / entriesPerPage;
	}

	/**
	 * Convert Markdown to HTML content (in an amazingly unoptimized way)
	 * 
	 * @param content
	 *            some Markdown content
	 * @return that content in plain and sanitized HTML (XSS safe!)
	 */
	public static String markdownToHtml(String content) {
		return Jsoup.clean(new PegDownProcessor().markdownToHtml(content),
				Whitelist.basic());
	}

	/**
	 * Strip all HTML tags.
	 * 
	 * @param content
	 *            the HTML content to strip the tags from
	 * @return the string
	 */
	public static String htmlToText(String content) {
<<<<<<< HEAD
		content = content == null ? "" : content;
		return StringEscapeUtils.unescapeHtml(Jsoup.clean(content,
				Whitelist.none()));
=======
		return StringEscapeUtils.unescapeHtml(Jsoup.clean(content, Whitelist
				.none()));
	}

	/**
	 * Generate a String with random chars
	 * 
	 * @param length
	 *            of the String
	 * @return the string
	 */
	public static String randomStringGenerator(int length) {
		char[] letters = new char[26];
		char[] buffer = new char[length];
		Random random = new Random();
		for (int i = 0; i < 26; i++)
			letters[i] = (char) ('a' + i);

		for (int i = 0; i < length; i++)
			buffer[i] = letters[random.nextInt(letters.length)];

		return new String(buffer);
>>>>>>> 96254e150794b2745efee784518b493b68981bcd
	}
}
