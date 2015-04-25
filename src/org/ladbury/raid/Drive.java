package org.ladbury.raid;

public class Drive extends RaidMember {

	int capacity, read, write;
	
	public Drive(int cap, int read, int write) {
		this.capacity = cap;
		this.read = read;
		this.write = write;
	}
	
	@Override
	public int getCapacity() {
		return this.capacity;
	}

	@Override
	public int getReadSpeed() {
		return this.read;
	}

	@Override
	public int getWriteSpeed() {
		return this.write;
	}

	@Override
	public String toString() {
		return "[" + capacity + "," + read + "," + write + "]";
	}

}
