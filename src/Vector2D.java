/**
 * Vecteur en 2 dimensions (x et y)
 * Permet de manipuler plus simplement les vitesses et les forces
 */
public class Vector2D {

    public double x;
    public double y;

    /**
     * Constructeur
     * Crée un vecteur en (x;y)
     *
     * @param x
     * @param y
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur
     * Crée un vecteur identique à v
     *
     * @param v Vecteur à copier
     */
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Constructeur
     * Crée un vecteur en (0;0)
     */
    public Vector2D() {
        x = 0;
        y = 0;
    }

    /**
     * Ajoute un vecteur
     *
     * @param v Vecteur à ajouter
     * @return Vecteur additionné
     */
    public Vector2D add(Vector2D v) {
        x += v.x;
        y += v.y;

        return this;
    }

    /**
     * Soustrait un vecteur
     *
     * @param v Vecteur à soustraire
     * @return Vecteur soustrait
     */
    public Vector2D subtract(Vector2D v) {
        x -= v.x;
        y -= v.y;

        return this;
    }

    /**
     * Multiplie par un nombre
     *
     * @param number nombre à multiplier
     * @return Vecteur multiplié
     */
    public Vector2D multiply(double number) {
        x *= number;
        y *= number;

        return this;
    }

    /**
     * Divise par un nombre
     *
     * @param number Nombre à diviser
     * @return Vecteur divisé
     */
    public Vector2D divide(double number) {
        if (number != 0) {
            x /= number;
            y /= number;
        }

        return this;
    }

    /**
     * Renvoie la norme du vecteur
     *
     * @return norme
     */
    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalise la norme
     *
     * @return Vecteur normalisé
     */
    public Vector2D normalized() {
        if (norm() > 0) {
            return new Vector2D(x / norm(), y / norm());
        } else {
            return this;
        }
    }

    /**
     * Changer la norme du vecteur
     *
     * @param scaleValue nouvelle norme
     * @return Vecteur modifié
     */
    public Vector2D scaleNorm(double scaleValue) {
        scaleValue = Math.abs(scaleValue);

        x = ((x / norm()) * scaleValue);
        y = ((y / norm()) * scaleValue);

        return this;
    }

    /**
     * Additionne 2 vecteurs
     *
     * @param v1 Vecteur 1
     * @param v2 Vecteur 2
     * @return Les 2 vecteurs additionnés
     */
    public static Vector2D add(Vector2D v1, Vector2D v2) {
        double x = v1.x + v2.x;
        double y = v1.y + v2.y;

        return new Vector2D(x, y);
    }

    /**
     * Soustrait 2 vecteurs
     *
     * @param v1 Vecteur 1
     * @param v2 Vecteur 2
     * @return Les 2 vecteurs soustraits
     */
    public static Vector2D subtract(Vector2D v1, Vector2D v2) {
        double x = v1.x - v2.x;
        double y = v1.y - v2.y;

        return new Vector2D(x, y);
    }

    /**
     * Renvoit le produit scalaire de 2 vecteurs
     *
     * @param v1 Vecteur 1
     * @param v2 Vecteur 2
     * @return Le produit scalaire des 2 vecteurs
     */
    public static double scalarProduct(Vector2D v1, Vector2D v2) {
        return (v1.x * v2.x) + (v1.y * v2.y);
    }

    @Override
    public String toString() {
        return "{" + x + ";" + y + "}";
    }
}

