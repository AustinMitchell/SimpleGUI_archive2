package testers.random_map;
import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.textarea.Label;
import simple.gui.textarea.TextArea;
import simple.run.*;
import java.awt.Color;
import java.awt.image.DataBufferInt;
import java.util.*;;

public class Main extends SimpleGUIApp {
	// Interface for performing some task
	interface Worker<ResultType> {
		ResultType doWork(List inputs);
	}
	// Thread that performs some task designated by a Worker
	class WorkerThread<ResultType> implements Runnable {
		private Worker<ResultType> worker;
		private List inputs;
		private boolean finished;
		private ResultType result;
		
		// Set inputs for worker
		public void setInputs(List inputs) { this.inputs = inputs;}
		
		public WorkerThread(Worker<ResultType> worker, List inputs) {
			this.worker = worker;
			finished   = false;
			this.inputs = inputs;
		}
		
		public void run() {
			finished = false;
			// Performs a task with given inputs. This function is run during a call to start() from a Thread object
			result = worker.doWork(inputs);
			finished = true;
		}
		
		public boolean isFinished() { return finished; }
		public ResultType getResult() { finished = false; return result; }
	}
	
	public static void main(String[]args) { Main.start(new Main(), "Map Generator"); }
	public Main() { super(1200, 1000, 30); }
	
	int depth = 9;
	int size = (1<<depth) + 1;
	float min = 0;
	float max = 255;
	float h = 0.5f;
	
	// Scored screen widgets that aren't the imagebox
	ScaledPanel screen;
	
	Map map;
	
	// Worker thread to generate heightmap
	WorkerThread<Map>   generatorThread;
	// Worker thread to convert map to a viewable image
	WorkerThread<ImageBox> converterThread;
	
	// Button to get a new image
	Button reset;
	
	// Slider to set roughness from 0-1
	Slider roughnessSlider;
	Label  roughnessLabel;
	
	// Slider to set depth from 0-15
	Slider depthSlider;
	Label  depthLabel;
	Label  sizeLabel;
	
	// Result image from map
	ImageBox image;
	
	// Indicates that a new map is generating/converting
	boolean loading;
	
	// Offset for image's origin
	int offsetX, offsetY;
	
	// initial setup for the program
	public void setup() {
		reset = new Button("Generating...", null);
		reset.setFillColor(new Color(255, 200, 200));
		
		roughnessSlider = new Slider(0, 100, false, true);
		roughnessSlider.setValue((int)(h*100));
		roughnessSlider.setMouseScrollIncrement(5);
		roughnessLabel  = new Label(" H value: " + h);
		roughnessLabel.setAlignment(TextArea.Alignment.WEST);
		roughnessLabel.setBoxVisible(true);
		
		depthSlider = new Slider(0, 15, false, true);
		depthSlider.setValue((int)(depth));
		depthLabel  = new Label(" Depth: " + depth);
		depthLabel.setAlignment(TextArea.Alignment.WEST);
		depthLabel.setBoxVisible(true);
		sizeLabel   = new Label(" Size: " + size + " x " + size);
		sizeLabel.setAlignment(TextArea.Alignment.WEST);
		sizeLabel.setBoxVisible(true);
				
		screen = new ScaledPanel(0, 0, getWidth(), getHeight(), 1000, 1000);
		screen.addWidget(reset,           880,  20, 100, 50, 1);
		screen.addWidget(roughnessSlider, 830,  90, 150, 40, 1);
		screen.addWidget(roughnessLabel,  830, 133, 150, 40, 1);
		screen.addWidget(depthSlider,     830, 190, 150, 40, 1);
		screen.addWidget(depthLabel,      830, 233, 150, 40, 1);
		screen.addWidget(sizeLabel,       830, 276, 150, 40, 1);
		
		image = new ImageBox();
		image.setSize(size, size);
		
		/** Simply returns a new instance of Map with constant values
		 * Expects arguments:
		 * 		0 - depth
		 * 		1 - min
		 *  	2 - max
		 *  	3 - h (roughness)
		*/
		generatorThread = new WorkerThread<Map>(new Worker<Map>() {
				public Map doWork(List inputs) {
					return new Map((Integer)inputs.get(0), (Float)inputs.get(1), (Float)inputs.get(2), (Float)inputs.get(3));
				}
			}, Arrays.asList(depth, min, max, h));
		
		/** Converts map from input set to an image. Starts with no args set as they will be set later
		 * Expects arguments:
		 * 		0 - size (image will have dimensions sizexsize)
		 * 		1 - Map object
		 */
		converterThread = new WorkerThread<ImageBox>(new Worker<ImageBox>() {
				public ImageBox doWork(List inputs) {
					ImageBox image = new ImageBox(new Image((Integer)inputs.get(0), (Integer)inputs.get(0)));
					convertToImage(image.image(), (Map)inputs.get(1));
					image.resetBaseImage();
					image.setSize(1000, 1000);
					return image;
				}
			}, null);
		
		loading = true;
		offsetX = 0;
		offsetY = 0;
		
		new Thread(generatorThread).start();
		
	}
	
	public void loop() {
		screen.update();
		image.update();
		
		if (roughnessSlider.valueChanged()) {
			h = roughnessSlider.value()/100f;
			roughnessLabel.setText("  H value: " + h);
		}
		
		if (depthSlider.valueChanged()) {
			depth = depthSlider.value();
			size = (1<<depth)+1;
			
			depthLabel.setText(" Depth: " + depth);
			sizeLabel.setText(" Size: " + size + " x " + size);
		}
		
		// if clicked and work is not currently being done, kick off generator thread
		if (reset.clicked() && !loading) {
			loading = true;
			reset.setText("Generating...");
			reset.setFillColor(new Color(255, 200, 200));
			
			// Sets new map parameters as arguments for generatorThread
			generatorThread.setInputs(Arrays.asList(depth, min, max, h));
			new Thread(generatorThread).start();
		}
		
		// once generation is done, kick off conversion thread
		if (generatorThread.isFinished()) {
			reset.setText("Converting color...");
			map = generatorThread.getResult();
			reset.setFillColor(new Color(255, 255, 200));
			
			// Sets new map as argument for converterThread
			converterThread.setInputs(Arrays.asList(size, map));
			new Thread(converterThread).start();
		}
		// Once done, set local image to resulting image
		if (converterThread.isFinished()) {
			loading = false;
			reset.setText("Generate");
			reset.setFillColor(new Color(255, 255, 255));
			
			image = converterThread.getResult();
			
			offsetX = Math.min(getWidth(), Math.max(-size, offsetX-Input.mouseOldX()+Input.mouseX()));
			offsetY = Math.min(getHeight(), Math.max(-size, offsetY-Input.mouseOldY()+Input.mouseY()));
			image.setLocation(offsetX, offsetY);
		}
		
		if (Input.mousePressed() && !reset.containsMouse() && !roughnessSlider.containsMouse() && !depthSlider.containsMouse()) {
			offsetX = Math.min(getWidth(), Math.max(-size, offsetX-Input.mouseOldX()+Input.mouseX()));
			offsetY = Math.min(getHeight(), Math.max(-size, offsetY-Input.mouseOldY()+Input.mouseY()));
			image.setLocation(offsetX, offsetY);
		}
		
		image.draw();
		screen.draw();
		updateView();
	}
	
	// Converts heightmap (expecting min/max values to be 0-255) to a colored image
	void convertToImage(Image image, Map map) {
		int[] colorMap = new int[256];
		for (int i=0; i<256; i++) {
			colorMap[i] = toFireColor(i);
		}
		
		int[] imagePixels = image.getPixelsNoCopy();
		
		int current = 0;
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				int val = (int)map.get(i, j);
				imagePixels[current+j] = colorMap[val];
			}
			current += size;
		}
	}
	
	// Maps to a general fire-like color pattern
	int toFireColor(int val) {
		if (val < 85) {
			// sets red from 0-255
			return (0xff<<24) | ((val*3)<<16) | (0x00<<8)         | (0x00);
		} else if (val < 170) {
			// sets green from 0-170
			return (0xff<<24) | (0xff<<16)    | (((val-85)*2)<<8) | (0x00);
		} else {
			// sets green from 170-255 and blue from 0-64
			return (0xff<<24) | (0xff<<16)    | (val<<8)          | (val/4);
		}
	}

}
