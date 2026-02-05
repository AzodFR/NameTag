# NameTag V2.0 

## How to create custom behavior

1 - Install this mod in your mod folder
2 - Create a pack
3 - In your `resources/Server` folder create a folder `NameTag`
4 - Create a json file with whatever name you like (`Example.json`)

### JSON Structure

`TagName`         (String)    -> The name tag for which your behavior will be applied
`IgnoreCase`      (Boolean)   -> If you want to not check letter casing
`AllowedNPCRoles` ([]String)  -> List of NPC role for which your tag should apply (empty or non-existent mean all)
---
`LogMessage`  (String) -> Debug Message in Server console
----
`ChangeAsset`           (Custom structure) -> Change model asset with the one you want
    -> `TargetAsset`    (String)           -> Asset that will be applied (Should be present in `Server/Model`, custom model accepted)
    -> `RemoveOnChange` (Boolean)          -> If true, when another NameTag is applied, original model come back
----
`PlayAnimation`          (Custom structure) -> Play an animation that the Entity know
    -> `AnimationName`   (String)           -> Animation to play (Entity **should** know the animation, custom one accepted but need to be registered in entity)
    -> `RemoveOnChange` (Boolean)          -> If true, when another NameTag is applied, stop animation


### Example Files

```json
{
  "TagName": "LOG",
  "IgnoreCase": true,
  "LogMessage": "THIS IS A LOG"
}
```

```json
{
  "TagName": "Technoblade",
  "IgnoreCase": true,
  "AllowedNPCRoles": [
    "Pig"
  ],
  "ChangeAsset": {
    "TargetAsset": "Pig_Technoblade",
    "RemoveOnChange": true
  }
}
```

```json
{
  "TagName": "Idle",
  "PlayAnimation": {
    "AnimationName": "Idle",
    "RemoveOnChange": true
  }
}
```