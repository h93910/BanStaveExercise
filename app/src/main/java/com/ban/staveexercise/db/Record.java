package com.ban.staveexercise.db;

import java.util.Date;

public class Record {
	private Date time;
	private short correct;
	private short incorrect;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public short getCorrect() {
		return correct;
	}

	public void setCorrect(short correct) {
		this.correct = correct;
	}

	public short getIncorrect() {
		return incorrect;
	}

	public void setIncorrect(short incorrect) {
		this.incorrect = incorrect;
	}

	@Override
	public String toString() {
		return "Record [time=" + time + ", corrent=" + correct + ", incorrent="
				+ incorrect + "]";
	}

}
