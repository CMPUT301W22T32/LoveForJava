# LoveForJava
1.Product Backlog

main u:
click to scan (3, low)
check the map (7, medium)
see the rank (1.5, low)

map
see the QR code location with score on map (5, high)

scan 
scan QR code (4, medium)

rank list
check the rank list of score (5, medium)

User profile page
user QR code (5, high)
friend list (3, high)
list of your QR codes (3, high)
Remove QR codes

QR code page	
have comments (3 medium)
likes (4 medium)
gallery (2, medium)
flag (2, medium)

Login page: 
exit (1.5, low)
login (3, high)

Afterscan page:
record location (2, medium)
score  (1, medium)
photograph location  (3 medium)
save qr code to user profile(4 high)

2.User Interface Mockups and Storyboard Sequences:
<img width="870" alt="Screen_Shot_2022-02-14_at_3 43 56_PM" src="https://user-images.githubusercontent.com/91228828/153959663-8b76a673-f648-495b-b810-fa09bd0d4f40.png">

storyboard:
main ui: click to scan, check the map, see the rank 
map: see the QR code location with score on map 
scan: scan QR code
rank list: check the rank list of scores 
User profile page: user QR code, friend list, list of your QR codes
QR code page: have comments, likes, gallery, flag
login page: exit, login
Afterscan page: geo location, score, photograph location

		
		


3.Object-Oriented Analysis:(CRC cards)

  1.Class : Player 
  Collaborators : QR code
  Responsibility : See own profile, automatically log into own account as the device can identify the player, Add new QR codes, See list of QR codes added to account, remove QR codes, see highest and lowest scoring QR codes, see sum of scores of QR codes scanned, see total number of QR codes scanned, see other player’s profiles
  2.Class : Owner
  Collaborators : Player
  Responsibility : Store small images online ? , delete QR codes that are bad or malicious, delete players
  3.Class : Search
  Collaborators : Player, Location
  Responsibilities : search for other players by username, search for nearby QR codes
  4.Class : Location
  Collaborators : Search, Map
  Responsibilities : see a map of nearby QR codes
  5.Class : Scoring 
  Collaborators : QR code, Player, Game
  Responsibilities : do all the necessary calculations
  6.Class : QR code 
  Collaborators : Player, Owner, Search, Location, Scoring 
  Responsibilities : keep track of who has seen it, hash and store without retaining private info
  7.Class: Database
	Collaborators : Player, Owner, Search, Location
  Responsibilities : store key value pairs of location and score, store players with unique user id, store owners with list of owned players, be searchable
  8.Class : Camera
	Collaborators : QR code
	Responsibilities : Scan QR codes
  9.Class : Game
	Collaborators : Scoring
	Responsibilities : see game-wide high scores of all players, see an estimate of my ranking for the highest scoring unique QR code, see an estimate of my ranking     for the total number of QR codes scanned, see an estimate of my ranking for a total sum of scores of QR codes scanned
  10.Class: QR code page
	Collaborators : QR code, Location, Player
	Responsibilities: Flag malicious code, comment on code, gallery view of images, 
  11.Class: Map
	Collaborators : Location
	Responsibilities: visualize the list of locations

4.Tool list:
	used the github project dashboard to do the tasks management 
	repo the project on github 
	use discord as main communication tool
