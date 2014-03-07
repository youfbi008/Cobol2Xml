package koopa.trees.antlr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import koopa.app.cli.ToXml;
import koopa.tokens.Token;
import koopa.tokens.Position;
import koopa.util.ANTLR;
import koopa.util.Tuple4;

import org.antlr.runtime.tree.CommonTree;

public class CommonTreeSerializer {

	private static final boolean INCLUDE_POSITIONING;
	private static String enterkey = "\r\n";
	private static String code = "";
	private static boolean blInputFile = false;
	static {
		final String property = System.getProperty(
				"koopa.xml.include_positioning", "false");

		boolean includePositioning;
		try {
			includePositioning = Boolean.valueOf(property);

		} catch (Exception e) {
			includePositioning = false;
		}

		INCLUDE_POSITIONING = includePositioning;

	}

	public static int getLineNum(int lineNum) {
		if(!blInputFile) {
			return lineNum + 1;
		}
		return lineNum;
	}
	public static void serialize(CommonTree tree, File file, boolean isInputFile) throws IOException {
		// Read file content to build a string.
		blInputFile = isInputFile;
		StringBuffer buf = null;
		BufferedReader bufFormStdIn = new BufferedReader(
						new InputStreamReader(System.in));
		buf = new StringBuffer();
        while (bufFormStdIn.ready()) {
            buf.append((char) bufFormStdIn.read());
        }
        bufFormStdIn.close();
        code = buf.toString();

		Writer writer = new BufferedWriter(new FileWriter(file));
		serialize(tree, writer);
	}

	public static String serialize(CommonTree tree) throws IOException {
		StringWriter writer = new StringWriter();
		serialize(tree, writer);
		return writer.toString();
	}

	public static void serialize(CommonTree tree, Writer writer)
			throws IOException {
		TokenTypes types = new ANTLRTokenTypesLoader()
				.load("/koopa/grammars/cobol/antlr/Cobol.tokens");

		writer.append("<koopa>" + enterkey);
		walk(writer, tree, "  ", types);
		writer.append("</koopa>" + enterkey);

		writer.flush();
		writer.close();
	}

	private static Tuple4<Integer, Integer, Integer, Integer> getTokenPosition(CommonTree tree) {
//		Token aa =tree.getToken();
//		Position a  = aa.getStart();

		int startline = tree.getLine();
		int endline = startline;
		int startpos = tree.getToken().getCharPositionInLine() - 1;
		int endpos = startpos + tree.getText().length();

		int lineHeadIndex = 0;
		int nextLineHeadIndex = 0;
		int i = 1;
		for (; i < startline; i++) {
			lineHeadIndex = code.indexOf('\n', lineHeadIndex) + 1;
		}
		nextLineHeadIndex = code.indexOf('\n', lineHeadIndex) + 1;
		int startLineCharNum = nextLineHeadIndex - lineHeadIndex;
		// TODO /r need be consider.
		if(startLineCharNum < (startpos + tree.getText().length())) {
			endline = startline + 1;
			endpos = startpos + tree.getText().length() - startLineCharNum + 7;
		}

		return new Tuple4<Integer, Integer, Integer, Integer>(startline, endline, startpos, endpos);
	}

	private static void walk(Writer writer, CommonTree tree, String dent,
			TokenTypes types) throws IOException {

		final int type = tree.getType();

		if (types.isLiteral(type) || types.isToken(type)) {
			// TODO Should escape stuff where necessary.
		//	Tuple4<Integer, Integer, Integer, Integer> token = getTokenPosition(tree);
			// Get the token data
			Token koopaToken = ((CommonKoopaToken)tree.token).getKoopaToken();

			writer.append(dent + "<TOKEN startline=\"" + getLineNum(koopaToken.getStart().getLinenumber()) +
					"\" startpos=\"" + koopaToken.getStart().getOffsetPositionInLine() +
					"\" endline=\"" + getLineNum(koopaToken.getEnd().getLinenumber()) +
					"\" endpos=\"" + (koopaToken.getEnd().getOffsetPositionInLine() + 1) + "\">" +
						tree.getText() + "</TOKEN>" + enterkey);
			return;
		}

		if (tree.getChildCount() == 0) {
			writer.append(dent + "<" + tree.getText());
			writePositions(writer, tree);
			writer.append(" />" + enterkey);

		} else {
			writer.append(dent + "<" + tree.getText());
			writePositions(writer, tree);
			writer.append(">" + enterkey);

			for (Object child : tree.getChildren()) {
				walk(writer, (CommonTree) child, dent + "  ", types);
			}

			writer.append(dent + "</" + tree.getText() + ">" + enterkey);
		}
	}

	private static void writePositions(Writer writer, CommonTree tree)
			throws IOException {

		if (!INCLUDE_POSITIONING) {
			return;
		}

		final Position start = ANTLR.getStart(tree);
		final Position end = ANTLR.getEnd(tree);

		if (start != null) {
			writer.append(" from=\"" + start.getPositionInFile() + "\"");
			writer.append(" from-line=\"" + start.getLinenumber() + "\"");
			writer.append(" from-column=\"" + start.getPositionInLine() + "\"");
		}

		if (end != null) {
			writer.append(" to=\"" + end.getPositionInFile() + "\"");
			writer.append(" to-line=\"" + end.getLinenumber() + "\"");
			writer.append(" to-column=\"" + end.getPositionInLine() + "\"");
		}
	}
}
