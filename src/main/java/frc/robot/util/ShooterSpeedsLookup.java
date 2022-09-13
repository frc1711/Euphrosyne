package frc.robot.util;

public class ShooterSpeedsLookup {
    
    private ShooterSpeedsLookup () { }
    
    // Must be ordered from least to greatest distance
    private static final ShooterSpeed[] allSpeeds = {
        // current:         116.7           -0.40           0.43
        //               distance    upper speed     lower speed
        new ShooterSpeed(   70.7,           -0.14,          0.53),
        new ShooterSpeed(   106.7,          -0.335,         0.435),
        new ShooterSpeed(   116.7,          -0.39,          0.42),
        new ShooterSpeed(   132.7,          -0.41,          0.45), // questionable
        new ShooterSpeed(   174.7,          -0.41,          0.45),
        new ShooterSpeed(   196.2,          -0.56,          0.52),
    };
    
    public static ShooterSpeed getShooterSpeed (double distance) {
        // If the distance is less than the lowest distance, return the lowest speed
        if (distance <= allSpeeds[0].distance) return allSpeeds[0];
        
        // If the distance is greater than the highest distance, return the highest speed
        if (distance >= allSpeeds[allSpeeds.length - 1].distance) return allSpeeds[allSpeeds.length - 1];
        
        // Get the index of the shooter speed with distance that just barely overshoots
        int highDistIndex = 0;
        for (int i = 0; i < allSpeeds.length; i ++) {
            
            // Found the highest speed that will just barely overshoot
            if (distance < allSpeeds[i].distance) {
                highDistIndex = i;
                break;
            }
        }
        
        final ShooterSpeed undershoot = allSpeeds[highDistIndex - 1], overshoot = allSpeeds[highDistIndex];
        
        // Interpolate between the two points for both upper and lower speeds
        final LinearInterpolator upperSpeed = new LinearInterpolator(
            undershoot.distance, undershoot.upperSpeed,
            overshoot.distance, overshoot.upperSpeed);
        final LinearInterpolator lowerSpeed = new LinearInterpolator(
            undershoot.distance, undershoot.lowerSpeed,
            overshoot.distance, overshoot.lowerSpeed);
        
        return new ShooterSpeed(distance, upperSpeed.interpolate(distance), lowerSpeed.interpolate(distance));
    }
    
    public static class ShooterSpeed {
        public final double distance, upperSpeed, lowerSpeed;
        private ShooterSpeed (double distance, double upperSpeed, double lowerSpeed) {
            this.distance = distance;
            this.upperSpeed = upperSpeed;
            this.lowerSpeed = lowerSpeed;
        }
    }
    
}