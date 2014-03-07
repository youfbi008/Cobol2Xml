package koopa.util;

public class Tuple4<T, U, P, L> {
	private T first = null;
	private U second = null;
	private P third = null;
	private L fourth = null;

	public Tuple4(T first, U second, P third, L fourth) {
		this.first = first;
		this.second = second;
		this.setThird(third);
		this.setFourth(fourth);
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public U getSecond() {
		return second;
	}

	public void setSecond(U second) {
		this.second = second;
	}

	public P getThird() {
		return third;
	}

	public void setThird(P third) {
		this.third = third;
	}

	public L getFourth() {
		return fourth;
	}

	public void setFourth(L fourth) {
		this.fourth = fourth;
	}
}
