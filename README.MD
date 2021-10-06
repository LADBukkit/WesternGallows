# WesternGallows
A bukkit plugin that allows executioners to hang players at gallows.

## Configure a Gallow
First use `/gallow create <name>` to create a gallow.  
Then use `/gallow setpoint <name>` to set the position of the criminal.  
After that use `/gallow setlead <name>` to set the position of the lead.
Here you have to look at a fence, so it knows where to tie the knot to.  
To finish the gallow use `/gallow setlever <name>` while looking at a lever to set the position of the lever.

## Hanging a Player
Hanging a player is as simple as `/hang <player> <name>` where name is the name of the gallow.  
Then you have to press the lever to kill them.

## Permissions
| Permission     | Usage                                |
|----------------|--------------------------------------|
| wg.admin       | For the configuration of the gallows |
| wg.executioner | For hanging a player                 |
| wg.ignorehang  | To shield a player from getting hung |