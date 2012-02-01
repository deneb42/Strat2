# Test server for Strat 
import socket, time
from collections import deque
from threading import Thread

# Configuration
HOST = '0.0.0.0' # Everybody
PORT = 50000     # The port

srv_messages = {
	'STEAL_STONE' : 0,
	'GIVE_STONE' : 1,
	'USE_BONUS' : 2
}

# Open, bind and listen
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)

# Acccept a guy
conn, addr = s.accept()
print 'Connected by', addr

# Send thread
class SendThread(Thread):
	def __init__(self):
		Thread.__init__(self)
		self.running = True

	def stop(self):
		self.running = False

	def run(self):
		while self.running:
			print 'Quelle action ?'
			print '1) START_GAME'
			print '2) STONE_QUANTITY'
			print '3) ACTION_GAUGE'
			print '4) OBTAIN_BONUS'
			print '5) DISCONNECT'
			print '6) END_GAME'
			print '7) Quit'

			try:
				action = int(raw_input())

				if action == 7:
					self.running = False

				if action == 1:
					theid = chr(int(raw_input('ID du joueur ?')))
					totalid = chr(int(raw_input('Total de joueurs ?')))
					conn.send('\x00' + theid + totalid)

				if action == 2:
					stones = chr(int(raw_input('How many stones ?')))
					conn.send('\x01' + stones)

				if action == 3:
					actions = chr(int(raw_input('How many actions ?')))
					conn.send('\x02' + actions)

				if action == 4:
					bonus = chr(int(raw_input('Which bonus ?')))
					conn.send('\x03' + bonus)

				if action == 5:
					bonus = chr(int(raw_input('Disconnect who ?')))
					conn.send('\x04' + bonus)

				if action == 6:
					status = chr(int(raw_input('Lost (0) Win (1) ?')))
					if status in ('\x00', '\x01'):
						conn.send('\x05' + status)

			except KeyboardInterrupt:
				self.running = False

			except:
				pass
			time.sleep(0.5)

# Serve
SendThread().start()
q = deque([])
def readnext():
	return ord(q.popleft())
try:
	while 1:
		data = conn.recv(1024)
		if not data: break
		q.extend(data)

		try:
			while(1):
				action = readnext()
				if action == srv_messages['STEAL_STONE']:
					who = readnext()
					print 'You want to steal a stone from', who
				elif action == srv_messages['GIVE_STONE']:
					who = readnext()
					print 'You want to give a stone to', who
				elif action == srv_messages['USE_BONUS']:
					bonus = readnext()
					whofrom = readnext()
					whoto = readnext()
					print 'You want to use bonus', bonus, 'from', whofrom, 'to', whoto
		except IndexError:
			pass
except KeyboardInterrupt:
	pass

# Close all
print 'Close'
conn.close()
s.close()
