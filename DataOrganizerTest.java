
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DataOrganizerTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSummarize() {
		DataOrganizer t = new DataOrganizer();
		File input = new File("input.txt");
		//File input = new File("C:\\workspace\\RjavaTest\\src\\input.txt");
		//File input = new File("input.txt");
		assertTrue(input.exists());
		LinkedHashMap<String, LinkedHashMap<String, Integer>> hash = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		t.summarize(input, hash);

		for (String id : hash.keySet()) {
			assert id == "12345" || id == "12346" : id;
			System.out.print(id + " : ");
			for (String month : hash.get(id).keySet()) {
				System.out.print(month + "(" + hash.get(id).get(month) + "), ");
			}
			System.out.println();
		}
	}

	@Test
	public void testOutput() {
		File output = new File("output.txt");
		DataOrganizer t = new DataOrganizer();
		
		String[] values = {"12", "3", "4", "1", "4", "1234"};
		String[] id = {"123", "sdfsdf", "dsfa", "dadsf", "dsf", "sadfadsf"};
		t.output(output, id, values);
		assertTrue(output.exists());
		try ( FileReader fr = new FileReader(output);
			  BufferedReader input = new BufferedReader(fr)) {
			for (int i = 0; i < id.length; i++) {
				String s = input.readLine();
				assertEquals(s, id[i] + "\t" + values[i]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testWriteTrainingDataToFile() {
		DataOrganizer t = new DataOrganizer();
		File input = new File("input.txt");
		assertTrue(input.exists());
		LinkedHashMap<String, LinkedHashMap<String, Integer>> hash = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		t.summarize(input, hash);

		File output = new File("trainingData.txt");
		t.writeTrainingDataToFile(output, hash);
		assertTrue(output.exists() && output.length() > 0);
	}
}
