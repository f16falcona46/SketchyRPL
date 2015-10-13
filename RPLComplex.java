import java.util.ArrayList;
import java.util.Arrays;


public class RPLComplex extends RPLObject {
	private RPLReal realPart;
	private RPLReal imagPart;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"+","-","*","/","SIN","COS","TAN","ASIN","ACOS","ATAN","EXP","LN", "C->R","ABS","^","SETBLK","SETWHT","TSTPIX","RE","IM","SETLIN","EVAL"}));
	
	public RPLComplex(String init) {
		super(init);
	}

	@Override
	public void set(String input) {
		realPart = new RPLReal("0.0");
		imagPart = new RPLReal("0.0");
		input = input.substring( 1, input.length() - 1 );
		String[] cplxParts = input.split(",");
		realPart.set(cplxParts[0]);
		imagPart.set(cplxParts[1]);
	}

	@Override
	public int getType() {
		return 1;
	}

	@Override
	public String toString() {
		return "(" + realPart + "," + imagPart + ")";
	}

	@Override
	public ArrayList<String> supportedOps() {
		return supportedOpsAL;
	}
	
	@Override
	public void doOp(String op, RPLStack stack) throws BadOpException {
		try {
			super.doOp(op, stack);
		}
		catch(NoOpException eList) {
			try {//imag lv 1 real lv 2

				switch (supportedOpsAL.indexOf(op)) {
				case 0:	//+
					stack.doOp("SWAP"); //promote real to complex
					if (stack.get(0).getType()==0) {
						CommandParser.parse(stack, "0 R->C");
					}
					stack.doOp("SWAP");

					stack.doOp("C->R");
					CommandParser.parse(stack, "3 ROLL");
					stack.doOp("C->R");
					stack.doOp("SWAP");
					CommandParser.parse(stack, "4 ROLL");
					stack.doOp("+");
					CommandParser.parse(stack, "3 ROLLD");
					stack.doOp("+");
					stack.doOp("R->C");
					break;
				case 1:	//-
					stack.doOp("SWAP"); //promote real to complex
					if (stack.get(0).getType()==0) {
						CommandParser.parse(stack, "0 R->C");
					}
					stack.doOp("SWAP");

					stack.doOp("C->R");
					CommandParser.parse(stack, "3 ROLL");
					stack.doOp("C->R");
					stack.doOp("SWAP");
					CommandParser.parse(stack, "4 ROLL");
					stack.doOp("-");
					CommandParser.parse(stack, "3 ROLLD SWAP");
					stack.doOp("-");
					stack.doOp("R->C");
					break;
				case 2:	//*
					stack.doOp("SWAP"); //promote real to complex
					if (stack.get(0).getType()==0) {
						CommandParser.parse(stack, "0 R->C");
					}
					stack.doOp("SWAP");

					CommandParser.parse(stack, "2 PICK 2 PICK C->R DROP SWAP C->R DROP * 2 PICK 4 PICK C->R SWAP DROP SWAP C->R SWAP DROP * -"); //get real part
					CommandParser.parse(stack, "2 PICK 4 PICK C->R DROP SWAP C->R SWAP DROP * 3 ROLL 4 ROLL SWAP C->R DROP SWAP C->R SWAP DROP * +"); //get imaginary part
					CommandParser.parse(stack, "R->C"); //put the parts together and clean up
					break;
				case 3: //divide
					stack.doOp("SWAP"); //promote real to complex
					if (stack.get(0).getType()==0) {
						CommandParser.parse(stack, "0 R->C");
					}
					stack.doOp("SWAP");

					CommandParser.parse(stack, "SWAP 2 PICK C->R -1 * R->C *");
					CommandParser.parse(stack, "SWAP C->R 2 ^ SWAP 2 ^ + -1 ^ 0 R->C *");
					break;
				case 4: //SIN
					CommandParser.parse(stack, "DUP (0,1) * EXP SWAP (0,-1) * EXP - (0,2) /");
					break;
				case 5: //COS
					CommandParser.parse(stack, "DUP (0,1) * EXP SWAP (0,-1) * EXP + (2,0) /");
					break;
				case 6: //TAN
					CommandParser.parse(stack, "DUP SIN SWAP COS /");
					break;
				case 7: //ASIN
					CommandParser.parse(stack, "DUP (0,1) * SWAP (2,0) ^ (1,0) SWAP - (0.5,0) ^ + LN (0,-1) *");
					break;
				case 8: //ACOS
					CommandParser.parse(stack, "DUP (2,0) ^ (1,0) SWAP - (0.5,0) ^ (0,1) * - LN (0,1) *");
					break;
				case 9: //ATAN
					CommandParser.parse(stack, "DUP (0,1) + SWAP (0,1) SWAP - / LN (0,0.5) *");
					break;
				case 10: //EXP
					CommandParser.parse(stack, "C->R SWAP EXP 0 R->C SWAP DUP COS SWAP SIN R->C *");
					break;
				case 11: //LN
					CommandParser.parse(stack, "DUP ABS LN SWAP C->R");
					{
						RPLReal yRPL = (RPLReal)stack.pop();
						RPLReal xRPL = (RPLReal)stack.pop();
						stack.push(new RPLReal(Double.toString(Math.atan2(Double.parseDouble(yRPL.toString()),Double.parseDouble(xRPL.toString())))));
					}
					CommandParser.parse(stack, "R->C");
					break;
				case 12: //C->R
					stack.pop();
					stack.push(realPart);
					stack.push(imagPart);
					break;
				case 13: //ABS
					CommandParser.parse(stack, "C->R 2 ^ SWAP 2 ^ + 0.5 ^");
					break;
				case 14: //^
					stack.doOp("SWAP"); //promote real to complex
					if (stack.get(0).getType()==0) {
						CommandParser.parse(stack, "0 R->C");
					}
					stack.doOp("SWAP");

					CommandParser.parse(stack, "SWAP LN * EXP");
					break;
				case 15: //SETBLK
					stack.pop();
					stack.setPix(Double.parseDouble(realPart.toString()),Double.parseDouble(imagPart.toString()));
					break;
				case 16: //SETWHT
					stack.pop();
					stack.clearPix(Double.parseDouble(realPart.toString()),Double.parseDouble(imagPart.toString()));
					break;
				case 17: //TSTPIX
					stack.pop();
					if (stack.testPix(Double.parseDouble(realPart.toString()),Double.parseDouble(imagPart.toString()))) {
						stack.push(new RPLReal("1"));
					}
					else {
						stack.push(new RPLReal("0"));
					}
					break;
				case 18: //RE
					CommandParser.parse(stack, "C->R DROP");
					break;
				case 19: //IM
					CommandParser.parse(stack, "C->R SWAP DROP");
					break;
				case 20: //SETLIN
					{
						stack.pop();
						CommandParser.parse(stack, "C->R");
						RPLReal y1 = (RPLReal)stack.pop();
						RPLReal x1 = (RPLReal)stack.pop();
						stack.drawLine(Double.parseDouble(x1.toString()),Double.parseDouble(y1.toString()),Double.parseDouble(realPart.toString()),Double.parseDouble(imagPart.toString()));
					}
					break;
				}
			}
			catch (Exception e) {
				stack.push(this);
				throw new BadOpException();
			}
		}
	}

	@Override
	public RPLObject copy() {
		return new RPLComplex(this.toString());
	}

}
