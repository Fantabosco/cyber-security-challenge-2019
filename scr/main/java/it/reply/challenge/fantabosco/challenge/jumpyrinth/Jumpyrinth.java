package it.reply.challenge.fantabosco.challenge.jumpyrinth;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import it.reply.challenge.fantabosco.utils.FileUtils;

public class Jumpyrinth {
	
	private enum DirectionEnum {
		FROM_LEFT_TO_RIGHT,
		FROM_RIGHT_TO_LEFT,
		FROM_TOP_TO_BOTTOM,
		FROM_BOTTOM_TO_TOP		
	}
	
	private static List<String> file; 

	public static void main(String[] args) {
		file = FileUtils.readFile("jumpyrinth\\2c464e58-9121-11e9-aec5-34415dec71f2.txt");
		
		int x;
		int y;
		// Find every start
		for (x = 0; x < file.size(); x++) {
			for (y = 0; y < file.get(0).length(); y++) {
				// $ : Start of the path, move down of one position.
	            //  $ <- (1) Start from here.
	            //  x <- (2) Then move here.
				if(file.get(x).charAt(y) == '$') {
					Integer cycleCounter = solve(x + 1, y);
					if(cycleCounter != null) {
						System.out.println("Solved in " + cycleCounter + " cycles");
						System.out.println();
					}
				}
			}
		}
	}

	private static Integer solve(int x, int y) {
		int cycleCounter = 0;
		String flag = "";
		Deque<Character> stack = new ArrayDeque<>();
		while(true) {
			cycleCounter++;
			switch(file.get(x).charAt(y)) {
			case '@':
				// @ : End of the path, check the FLAG string. ;)
				if(flag.contains("FLG")) {
					System.err.println("FLAG: " + flag);
					return cycleCounter;
				} else {
					return null;
				}
			case '#':
				// # : Do nothing.
				System.err.println("NOP");
				return null;
			case '(':
				// ( : Pop from the STACK and prepend the char to the FLAG string, then jump to the left by the number of chars specified on the right.
				//		
				//		        xooooooooooo(12
				//		        ^           ^^
				//		        |           ||
				//		        |           |+- (2) Read this number.
				//		        |           +-- (1) Pop from the STACK and prepend the char to the FLAG string.
				//		        +-------------- (3) Then jump here.
				flag = stack.pop() + flag;
				y -= extractNumber(x, y + 1, DirectionEnum.FROM_LEFT_TO_RIGHT);	
				break;
			case ')':
				//) : Pop from the STACK and append the char to the FLAG string, then jump to the right by the number of chars specified on the left.
				//
				//        21)ooooooooooox
				//        ^ ^           ^
				//        | |           |
				//        | |           +- (3) Then jump here.
				//        | +------------- (1) Pop from the STACK and append the char to the FLAG string.
				//        +--------------- (2) Read this number.
				flag += stack.pop();
				y += extractNumber(x, y - 1, DirectionEnum.FROM_RIGHT_TO_LEFT);	
				break;
			case '-':
				//- : REMOVE THE FIRST CHAR OF THE FLAG STRING, THEN JUMP ABOVE BY THE NUMBER OF CHARS SPECIFIED BELOW.
				//
				//        X <- (3) THEN JUMP HERE.
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        O
				//        - <- (1) REMOVE THE FIRST CHAR OF THE FLAG STRING.
				//        1 <- (2) READ THIS NUMBER.
				//        2
				if(flag.length() > 1) {
					flag = flag.substring(1, flag.length());
				} else {
					if (flag.length() == 0) {
						System.err.println("Unexpectend command \"-\" on empty flag");						
					}
					flag = "";
				}
				x -= extractNumber(x + 1, y, DirectionEnum.FROM_TOP_TO_BOTTOM);	
				break;
			case '+':
				//+ : Remove the last char of the FLAG string, then jump below by the number of chars specified above.
				//
				//        2
				//        1 <- (2) Read this number.
				//        + <- (1) Remove the last char of the FLAG string.
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        x <- (3) Then jump here.
				if(flag.length() > 1) {
					flag = flag.substring(0, flag.length() - 1);
				} else {
					if (flag.length() == 0) {
						System.err.println("Unexpectend command \"-\" on empty flag");
					}
					flag = "";
				}
				x += extractNumber(x - 1, y, DirectionEnum.FROM_BOTTOM_TO_TOP);	
				break;
			case '%':
				//% : Reverse the FLAG string, then move down of one position.
				//
				//        % <- (1) Reverse the FLAG string.
				//        x <- (2) Then move here.
				flag = new StringBuilder(flag).reverse().toString();
				x++;
				break;
			case '[':
				//[ : Read the char to the right, push it into the STACK, than jump to the char at the right of it.
				//
				//        [cx
				//         ^^
				//         ||
				//         |+- (2) Then jump here.
				//         +-- (1) Read this char and push it into the STACK.
				stack.push(file.get(x).charAt(y + 1));
				y += 2;
				break;
			case ']':
				//] : Read the char to the left, push it into the STACK, than jump to the char at the left of it.
				//
				//        xc]
				//        ^^
				//        ||
				//        |+- (1) Read this char and push it into the STACK.
				//        +-- (2) Then jump here.
				stack.push(file.get(x).charAt(y - 1));
				y -= 2;
				break;
			case '*':
				//* : Read the char above it, push it into the STACK, than jump to the char above of that char.
				//
				//        x <- (2) Then jump here.
				//        c <- (1) Read this char and push it into the STACK.
				//        *
				stack.push(file.get(x - 1).charAt(y));
				x -= 2;
				break;
			case '.':
				//. : Read the char below it, push it into the STACK, than jump to the char below of that char.
				//
				//        .
				//        c <- (1) Read this char and push it into the STACK.
				//        x <- (2) Then jump here.
				stack.push(file.get(x + 1).charAt(y));
				x += 2;
				break;
			case '<':
				//< : Jump to the left by the number of chars specified on the right.
				//
				//        xooooooooooo<12
				//        ^            ^
				//        |            |
				//        |            +- (1) Read this number.
				//        +-------------- (2) Then jump here.
				y -= extractNumber(x, y + 1, DirectionEnum.FROM_LEFT_TO_RIGHT);	
				break;
			case '>':    
				//> : Jump to the right by the number of chars specified on the left.
				//
				//        21>ooooooooooox
				//        ^             ^
				//        |             |
				//        |             +- (2) Then jump here.
				//        +--------------- (1) Read this number.
				y += extractNumber(x, y - 1, DirectionEnum.FROM_RIGHT_TO_LEFT);	
				break;
			case '^':
				//^ : Jump above by the number of chars specified below.
				//
				//        x <- (2) Then jump here.
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        ^
				//        1 <- (1) Read this number.
				//        2
				x -= extractNumber(x + 1, y, DirectionEnum.FROM_TOP_TO_BOTTOM);	
				break;
			case 'v':
				//v : Jump below by the number of chars specified above.
				//
				//        2
				//        1 <- (1) Read this number.
				//        v
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        o
				//        x <- (2) Then jump here.
				x += extractNumber(x - 1, y, DirectionEnum.FROM_BOTTOM_TO_TOP);	
				break;
			default:
				System.err.println("Unexpected case: " + file.get(x).charAt(y));
			}
		}
	}
	
	private static int extractNumber(int i, int j, DirectionEnum direction) {
		int d_x = 0;
		int d_y = 0;
		switch(direction) {
		case FROM_BOTTOM_TO_TOP:
			d_x = -1;
			break;
		case FROM_LEFT_TO_RIGHT:
			d_y = 1;
			break;
		case FROM_RIGHT_TO_LEFT:
			d_y = -1;
			break;
		case FROM_TOP_TO_BOTTOM:
			d_x = 1;
			break;
		default:
			System.err.println("Unexpected case: " + direction);
			return 0;
		}
		
		int x = i;
		int y = j;
		String number = "";
		Character c;
		do {
			c = file.get(x).charAt(y);
			if(Character.isDigit(c)) {
				number += c;
			}
			x += d_x;
			y += d_y;
		} while(Character.isDigit(c));
		return Integer.parseInt(number);
	}
}
