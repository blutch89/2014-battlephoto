package ch.blutch.battlephoto.model.impl;

public class UserGrade {

	private int score;
	private int nbMedals;
	private Medal medal;
	
	public UserGrade() {
		
	}
	
	public UserGrade(int score) {
		this.score = score;
		calculateGrade();
	}
	
	public void addScore(int score) {
		this.score = score;
		calculateGrade();
	}
	
	private void calculateGrade() {
		// Calcul le nombre de médailles
		if (score >= 200) {
			this.nbMedals = 5;
			this.medal = Medal.STAR;
			
			return;
		}
		
		double modulo = (double) score % 50.0;
		this.nbMedals = (int) Math.ceil((modulo > 0.0 ? modulo : 1.0) / 10.0);
		
		// Détermine le type de médaille
		int result = (int) Math.floor(score / 50);
		
		switch (result) {
			case 0:
				this.medal = Medal.BRONZE;
				break;
			case 1:
				this.medal = Medal.SILVER;
				break;
			case 2:
				this.medal = Medal.GOLDER;
				break;
			case 3:
				this.medal = Medal.STAR;
				break;
			default:
				this.medal = Medal.STAR;
				break;
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNbMedals() {
		return nbMedals;
	}

	public void setNbMedals(int nbMedals) {
		this.nbMedals = nbMedals;
	}

	public Medal getMedal() {
		return medal;
	}

	public void setMedal(Medal medal) {
		this.medal = medal;
	}
	
}
