package it.polito.tdp.crimes.model;

public class Adiacenza {
		String tipo1;
		String tipo2;
		double peso;
		
		public Adiacenza(String tipo1, String tipo2, double peso) {
			super();
			this.tipo1 = tipo1;
			this.tipo2 = tipo2;
			this.peso = peso;
		}

		public String getTipo1() {
			return tipo1;
		}

		public void setTipo1(String tipo1) {
			this.tipo1 = tipo1;
		}

		public String getTipo2() {
			return tipo2;
		}

		public void setTipo2(String tipo2) {
			this.tipo2 = tipo2;
		}

		public double getPeso() {
			return peso;
		}

		public void setPeso(double peso) {
			this.peso = peso;
		}

		@Override
		public String toString() {
			return tipo1 + " - " + tipo2 + " = " + peso + "\n";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((tipo1 == null) ? 0 : tipo1.hashCode());
			result = prime * result + ((tipo2 == null) ? 0 : tipo2.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Adiacenza other = (Adiacenza) obj;
			if (tipo1 == null) {
				if (other.tipo1 != null)
					return false;
			} else if (!tipo1.equals(other.tipo1))
				return false;
			if (tipo2 == null) {
				if (other.tipo2 != null)
					return false;
			} else if (!tipo2.equals(other.tipo2))
				return false;
			return true;
		}
		
		
		
	
		
		
}
