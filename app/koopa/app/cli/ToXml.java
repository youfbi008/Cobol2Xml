package koopa.app.cli;

import java.io.File;
import java.io.IOException;

import koopa.parsers.ParseResults;
import koopa.parsers.cobol.CobolParser;
import koopa.tokenizers.cobol.SourceFormat;
import koopa.tokens.Token;
import koopa.trees.antlr.CommonTreeSerializer;
import koopa.util.Tuple;
import org.apache.log4j.Logger;
import org.antlr.runtime.tree.CommonTree;

public class ToXml {

	private static final int BAD_USAGE = -1;
	private static final int FILE_DOES_NOT_EXIST = -2;
	private static final int IOEXCEPTION = -3;

	private static final int INPUT_IS_INVALID = 1;
	private static final int INPUT_IS_VALID = 0;
	private static final Logger LOGGER = Logger.getLogger("parser");

	public static void main(String[] args) {
		if (args.length > 3) {
			System.out
					.println("Usage: GetASTAsXML [--free-format] <cobol-input-file> <xml-output-file>");
			System.exit(BAD_USAGE);
		}

		SourceFormat format = SourceFormat.FIXED;
		String inputFilename = null;
		String outputFilename = null;

		if (args.length == 3) {
			String option = args[0];
			if (option.equals("--free-format")) {
				format = SourceFormat.FREE;

			} else {
				System.out.println("Unknown option: " + option);
				System.exit(BAD_USAGE);
			}

			inputFilename = args[1];
			outputFilename = args[2];
		} else if (args.length == 2) {
//			String option = args[0];
//			if (option.equals("--free-format")) {
//				format = SourceFormat.FREE;
//
//			} else {
//				LOGGER.info("Unknown option: " + option);
//				System.exit(BAD_USAGE);
//			}

			inputFilename = args[0];
			outputFilename = args[1];
		} else if(args.length == 1) {

			outputFilename = args[0];
		}

		File cobolFile = null;
		if(inputFilename != null && inputFilename != "") {

			cobolFile = new File(inputFilename);
			if (!cobolFile.exists()) {
				LOGGER.info("Input file does not exist, so it will read data from standard input stream.");
				System.exit(FILE_DOES_NOT_EXIST);
			}
		}

		final CobolParser parser = new CobolParser();
		parser.setFormat(format);
		parser.setBuildTrees(true);

		ParseResults results = null;

		try {
			results = parser.parse(cobolFile);

		} catch (IOException e) {
			if(cobolFile != null) {
				LOGGER.info("IOException while reading " + cobolFile);
			} else {
				LOGGER.info("IOException while reading standard input stream.");
			}
			System.exit(IOEXCEPTION);
		}

		if (results.getErrorCount() > 0) {
			for (int i = 0; i < results.getErrorCount(); i++) {
				final Tuple<Token, String> error = results.getError(i);
				LOGGER.info("Error: " + error.getFirst() + " "
						+ error.getSecond());
			}
		}

		if (results.getWarningCount() > 0) {
			for (int i = 0; i < results.getWarningCount(); i++) {
				final Tuple<Token, String> warning = results.getWarning(i);
				LOGGER.info("Warning: " + warning.getFirst() + " "
						+ warning.getSecond());
			}
		}

		if (!results.isValidInput()) {
			LOGGER.info("Could not parse by reading standard input stream.");
			System.exit(INPUT_IS_INVALID);
		}

		final CommonTree ast = results.getTree();

		if(outputFilename != null) {
			final File xmlFile = new File(outputFilename);
			try {
				CommonTreeSerializer.serialize(ast, xmlFile);

			} catch (IOException e) {
				LOGGER.info("IOException while writing " + xmlFile);
				System.exit(IOEXCEPTION);
			}
		} else {
			try {
				String strXML = CommonTreeSerializer.serialize(ast);
				System.out.println(strXML);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		System.exit(INPUT_IS_VALID);
	}
}