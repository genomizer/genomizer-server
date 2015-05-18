package smoothing;


public class Tuple {
    private String chromosome;
    private int position;
    private double signal;
    private double newSignal;


    public Tuple(String strLine) {
		if (strLine != null){
			String[] tokens = strLine.split("\t");

			if (tokens.length == 3){
				chromosome = tokens[0];
				position = Integer.parseInt(tokens[1]);
				signal = Double.parseDouble(tokens[2]);
				newSignal = signal;
				//newSignal = -1;
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}

    }

    public double getNewSignal(){
		return newSignal;
    }

    public void setNewSignal(double d){
		newSignal = d;
    }

    public double getSignal() {
		return signal;
    }


    public void setSignal(double signal) {
		this.signal = signal;
    }


    public String getChromosome() {
		return chromosome;
    }


    public int getPosition() {
		return position;
    }

    public String toString(){
		if(chromosome == null){
			return "";
		}

		if ((newSignal == Math.floor(newSignal))) {
			return chromosome + "\t" + position + "\t" + (int)newSignal+ "\n";
		} else {
			return chromosome + "\t" + position + "\t"
					+((double)Math.round(newSignal * 100000) / 100000)+ "\n";
		}
    }

}
