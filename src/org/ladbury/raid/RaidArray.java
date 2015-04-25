package org.ladbury.raid;

import java.util.ArrayList;
import java.util.List;

public class RaidArray extends RaidMember {
	
	public enum RaidType {RAID0, RAID1, RAID5, RAID6};
	
	List<RaidMember> members;
	RaidType		 raidType;

	public RaidArray() {
		members = new ArrayList<RaidMember>();
	}
	public RaidArray(String type) {
		members = new ArrayList<RaidMember>();
		this.setType(type);
	}
	
	public void addRaidMember(RaidMember member) {
		if (!member.equals(null)) {
			members.add(member);
		}
	}
	
	public RaidMember getLast(){
		return this.members.get(this.members.size()-1);
	}
	
	public void setType(String type){
		if( type != null ) {
	        try {
	            raidType = RaidType.valueOf(type.toUpperCase());
	        } catch(IllegalArgumentException ex) {
	        	System.out.println("invalid Raid Type "+ type);
	        }
	    }
	}
	
	public String getType(){
		return raidType.toString();
	}
	int getMinMemberCapacity() {

		int min = 0;
		for  (RaidMember member : members) {
			if (min == 0) {
				min = member.getCapacity();
			} else {
				if (member.getCapacity() < min) min = member.getCapacity();
			}
		}
		return min;
	}
	
	int getMinMemberReadSpeed() {

		int min = 0;
		for  (RaidMember member : members) {
			if (min == 0) {
				min = member.getReadSpeed();
			} else {
				if (member.getReadSpeed() < min) min = member.getReadSpeed();
			}
		}
		return min;
	}
	
	int getMinMemberWriteSpeed() {

		int min = 0;
		for  (RaidMember member : members) {
			if (min == 0) {
				min = member.getWriteSpeed();
			} else {
				if (member.getWriteSpeed() < min) min = member.getWriteSpeed();
			}
		}
		return min;
	}
	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		switch (raidType){
		case RAID0:
			if (members.size() >0) {
				return this.getMinMemberCapacity() * this.members.size();
			} else {
				return 0;
			}
		case RAID1:
			if (members.size() >1) {
				return this.getMinMemberCapacity();
			} else {
				return 0;
			}	
		case RAID5:
			if (members.size() >2) {
				return this.getMinMemberCapacity() * (this.members.size()-1);
			} else {
				return 0;
			}
		case RAID6:
			if (members.size() >3) {
				return this.getMinMemberCapacity() * (this.members.size()-2);
			} else {
				return 0;
			}
		}
		return 0;
	}
	@Override
	public int getReadSpeed() {
		// TODO Auto-generated method stub
		switch (raidType){
		case RAID0:
			if (members.size() >0) {
				return this.getMinMemberReadSpeed() * this.members.size();
			} else {
				return 0;
			}
		case RAID1:
			if (members.size() >1) {
				return this.getMinMemberReadSpeed() * this.members.size();
			} else {
				return 0;
			}	
		case RAID5:
			if (members.size() >2) {
				return this.getMinMemberReadSpeed() * this.members.size();
			} else {
				return 0;
			}
		case RAID6:
			if (members.size() >3) {
				return this.getMinMemberReadSpeed() * this.members.size();
			} else {
				return 0;
			}	
		}
		return 0;
	}
	@Override
	public int getWriteSpeed() {
		// TODO Auto-generated method stub
		switch (raidType){
		case RAID0:
			if (members.size() >0) {
				return this.getMinMemberWriteSpeed() * this.members.size();
			} else {
				return 0;
			}
		case RAID1:
			if (members.size() >1) {
				return this.getMinMemberWriteSpeed();
			} else {
				return 0;
			}	
		case RAID5:
			if (members.size() >2) {
				return this.getMinMemberWriteSpeed() * (this.members.size()-1);
			} else {
				return 0;
			}
		case RAID6:
			if (members.size() >3) {
				return this.getMinMemberWriteSpeed() * (this.members.size()-2);
			} else {
				return 0;
			}	
		}
		return 0;
	}
	@Override
	public String toString() {
		String output = "";
		for (RaidMember rm : members) {
			if (output == "") {
				output = raidType.toString() + "{"+ rm.toString();
			} else {
				output += ","+ rm.toString();
			}
		}
		output += "}";
		return output;
	}

}
