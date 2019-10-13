package it.reply.challenge.fantabosco.challenge.matritext;

import java.util.Comparator;

public class Doc {
	
	public static class DocComparator implements Comparator<Doc> {
		@Override
		public int compare(Doc a, Doc b) {
			int author = a.author.compareTo(b.author);
			if(author != 0) {
				return author;
			}
			int title = a.title.compareTo(b.title);
			if(title != 0) {
				return title;
			}
			return 0;
		}
	}
	
	public String intro;
	public String license;
	public String title;
	public String author;
	public String file;
}
