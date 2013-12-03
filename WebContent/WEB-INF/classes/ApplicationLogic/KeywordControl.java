
package ApplicationLogic;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import DatabaseFrontend.Keyword;

public class KeywordControl {
	public final Control p;

	public KeywordControl(Control p) {
		this.p = p;
	}

	public boolean renameKeyword(String keyword, String newName) {
		try {
			Keyword l = Keyword.getKeywordByName(keyword);
			if (l == null)
				return false;
			return l.setName(newName);
		} catch (SQLException e) {
			return false;
		}
		
	}

	Set<String> wrap(Set<Keyword> keywords) {
		Set<String> toRet = new HashSet<String>();
		for (Keyword l : keywords)
			toRet.add(l.getName());
		return toRet;
	}

	public Set<Keyword> unwrap(Set<String> keywords) {
		Set<Keyword> toRet = new HashSet<Keyword>();
		for (String l : keywords)
			try {
				toRet.add(Keyword.getKeywordByName(l));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return toRet;
	}

	public Keyword unwrap(String keyword) {
		try {
			return Keyword.getKeywordByName(keyword);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}