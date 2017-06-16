/**
 * @author Yao Shi
 */

public class Data {

	public class Number {
		int location;
		int ones;
		int tens;
		int hundreds;

		// 0: top ; 1: middle; 2: bottom
		public Number(int location) {
			this.location = location;
			ones = 0;
			tens = 0;
			hundreds = 0;
		}

		public int getValue() {
			return ones + tens * 10 + hundreds * 100;
		}

		// 0: one ; 1: ten; 2: hundred
		public void setValue(int newValue, int place) {
			String result = "Set value successfully.";
			switch (place) {
			case 0:
				ones = newValue;
				break;
			case 1:
				tens = newValue;
				break;
			case 2:
				hundreds = newValue;
				break;
			default:
				result = "Invalid month.";
				break;
			}
			System.out.println(result);
		}
	}

	public Number top, middle, bottom;

	public Data() {
		top = new Number(0);
		middle = new Number(1);
		bottom = new Number(2);
	}

	public void getData() {
		System.out.println(top.getValue());
		System.out.println(middle.getValue());
		System.out.println(bottom.getValue());

	}
}
