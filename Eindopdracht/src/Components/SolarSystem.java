package Components;

import java.util.ArrayList;

public class SolarSystem{
    private String name;
    private Sun sun;
    private ArrayList<Planet> planets;
    public SolarSystem(String name, Sun sun){
        this.name = name;
        this.sun = sun;
        this.planets = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public Sun getSun(){
        return this.sun;
    }

    public void addPlanet(Planet planet){
        this.planets.add(planet);
    }

    public ArrayList<Planet> getPlanets(){
        return this.planets;
    }

}
