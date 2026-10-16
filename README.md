# yume-interpolation-fix

A mod that allows you to switch between the entity interpolation method introduced in Minecraft 26.3 and the previous interpolation method.

Adds an "Entity Interpolation" option under "Options" → "Accessibility Settings".

## Warning

This mod changes the movement paths of other entities and players as seen by the client.  
Since it alters vanilla (26.3) behavior and affects how easily entities can be hit, it may be considered cheating in a broad sense unless you have permission from the server administrator.  
Many anti-cheat systems are likely to validate the behavior of 26.3 clients based on the interpolation method used in 26.3, so this mod may be detected.  
Do not use this mod on servers without explicit permission from the server administrator.

## Building

```sh
./gradlew build
```

The resulting JAR file is located at `build/libs/yume-interpolation-fix-1.0.0.jar`.

## License

CC0-1.0
