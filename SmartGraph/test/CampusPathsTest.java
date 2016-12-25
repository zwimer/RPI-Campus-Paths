package SmartGraph.test;

import java.io.*;

import SmartGraph.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CampusPathsTest { // Rename to the name of your "main" class

	/**
	 * @param file1 
	 * @param file2
	 * @return true if file1 and file2 have the same content, false otherwise
	 * @throws IOException
	 */	
	/* compares two text files, line by line */
	private static boolean compare(String file1, String file2) throws IOException {
		BufferedReader is1 = new BufferedReader(new FileReader(file1)); // Decorator design pattern!
		BufferedReader is2 = new BufferedReader(new FileReader(file2));
		String line1, line2;
		boolean result = true;
		while ((line1=is1.readLine()) != null) {
			// System.out.println(line1);
			line2 = is2.readLine();
			if (line2 == null) {
				System.out.println(file1+" longer than "+file2);
				result = false;
				break;
			}
			if (!line1.equals(line2)) {
				System.out.println("Lines: "+line1+" and "+line2+" differ.");
				result = false;
				break;
			}
		}
		if (result == true && is2.readLine() != null) {
			System.out.println(file1+" shorter than "+file2);
			result = false;
		}
		is1.close();
		is2.close();
		return result;		
	}
	
	private void runTest(String filename) throws IOException {
		InputStream in = System.in; 
		PrintStream out = System.out;				
		String inFilename = "SmartGraph/data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "SmartGraph/data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "SmartGraph/data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out 
		CampusPaths.main(null); // Call to YOUR main. May have to rename.
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output
		assertTrue(compare(expectedFilename,outFilename));
	}
	
	//This function is the same as the function above, however it asserts
	//that the output and the expected output are different, this was made
	//for tests below which were used to increase coverage in the compare function above
	private void runTestandFail(String filename) throws IOException {
		InputStream in = System.in; 
		PrintStream out = System.out;				
		String inFilename = "SmartGraph/data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "SmartGraph/data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "SmartGraph/data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out 
		CampusPaths.main(null); // Call to YOUR main. May have to rename.
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output
		assertFalse(compare(expectedFilename,outFilename)); 
	}
	
	@Test
	public void testListBuildings() throws IOException {
		//Given by the instructor
		runTest("test1"); 
	}
	
	@Test
	public void testOneUnknownBuildingID() throws IOException {
		//Check to see the output for a path between a real place and a bad ID
		runTest("OneBadID"); 
	}
	
	@Test
	public void testOneUnknownBuildingName() throws IOException {
		//Check to see the output for a path between a real place and Fake_
		runTest("OneBadName"); 
	}
	
	@Test
	public void testTwoUnknownBuildingID() throws IOException {
		//Check to see the output for a path between a bad ID and a bad ID
		runTest("TwoBadID"); 
	}
	
	@Test
	public void testTwoUnknownBuildingName() throws IOException {
		//Check to see the output for a path between Fake_ and Fake2_
		runTest("TwoBadName"); 
	}
	
	@Test
	public void testTwoUnknownBuildingBoth() throws IOException {
		//Check to see the output for a path between a Fake_ and a bad ID
		runTest("TwoBadBoth"); 
	}
	
	@Test
	public void testTwoUnknownBuildingsSameID() throws IOException {
		//Check to see the output for a path between a bad ID and the same bad ID
		runTest("SameBadID"); 
	}	
	
	@Test
	public void testTwoUnknownBuildingsSameName() throws IOException {
		//Check to see the output for a path between Fake_ and Fake_
		runTest("SameBadName"); 
	}	
	
	@Test
	public void testNoPath() throws IOException {
		//Check to see the output for a path that doesn't exist
		runTest("NoPath"); 
	}	
	
	@Test
	public void testZeroLengthPath() throws IOException {
		//Check to see the output for a path that is of length 0
		runTest("ZeroLength");
	}
	
	@Test
	public void testDijkstras() throws IOException {
		//Check to see if Dijkstras' algo works when given only names
		runTest("Dijkstras");
	}
	
	@Test
	public void testDijkstrasID_Name() throws IOException {
		//Check to see if Dijkstras' algo works when given
		//one ID and one name or one name and one ID
		runTest("DijkstrasIDName");
	}
	
	@Test
	public void testDijkstrasIDS() throws IOException {
		//Check to see if Dijkstras' algo works when given only IDs
		runTest("DijkstrasIDS");
	}
	
	@Test
	public void PrintCommands() throws IOException {
		//This function checks to see that the print commands function works
		runTest("PrintCommands");
	}	
	
	
	//The following three tests exist solely for more code coverage in.
	//They each preform Dijkstras but their outbuts are either truncated
	//or extended so that the compare function above may have increased coverage
	@Test
	public void MisMatchedLine() throws IOException {
		//Two lines in the output are different
		runTestandFail("MisMatch");
	}	
	@Test
	public void TooShort() throws IOException {
		//Two lines in the expected output is too short 
		runTestandFail("TooShort");
	}	
	@Test
	public void TooLong() throws IOException {
		//Two lines in the expected output is too short
		runTestandFail("TooLong");
	}
}
