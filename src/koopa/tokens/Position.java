package koopa.tokens;

/**
 * Instances of this class should be immutable!
 */
public final class Position {

	private final int linenumber;
	private final int positionInFile;
	private final int positionInLine;

	public Position(int positionInFile, int linenumber, int positionInLine) {
		this.positionInFile = positionInFile;
		this.linenumber = linenumber;
		this.positionInLine = positionInLine;
	}

	public int getLinenumber() {
		return linenumber;
	}

	public int getOffsetLinenumber() {
		return linenumber + 1;
	}

	public int getPositionInFile() {
		return positionInFile;
	}

	public int getPositionInLine() {
		return positionInLine;
	}

	public int getOffsetPositionInLine() {
		return positionInLine - 1;
	}

	public Position offsetBy(int offset) {
		return new Position(positionInFile + offset, linenumber, positionInLine
				+ offset);
	}

	public String toString() {
		return linenumber + ":" + positionInLine;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Position)) return false;

		Position other = (Position) obj;
		return positionInFile == other.positionInFile;
	}
}
