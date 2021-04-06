

public class Checker {

    // public static final Pattern passPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    // passPattern.matcher(password).matches();

    public static boolean isStrong(String password)  // check if password has a lower,an  Upper case and a digit
    {

        return hasLowerCase(password) && hasUpperCase(password) && hasDigit(password) && hasRightLength(password);
    }
    public static boolean hasLowerCase(String password)
    {
        return password.chars().anyMatch(c-> Character.isLetter(c) && Character.isLowerCase(c));
    }
    public static boolean hasUpperCase(String password)
    {
        return password.chars().anyMatch(c-> Character.isLetter(c) && Character.isUpperCase(c));
    }
    public static boolean hasDigit(String password)
    {
        return password.chars().anyMatch(Character::isDigit);
    }
    public static boolean hasRightLength(String password)
    {
        return password.length() >= 6 && password.length() <= 20;
    }
    public static void main(String[] args) {
        System.out.println(isStrong("Anaaa12"));
    }
}
