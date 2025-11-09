# --1
# 	pip install matplotlib

# --2 
	
# 	notepad gantt.py

# --3    in notepad
	
	
import matplotlib.pyplot as plt
import datetime as dt

# ====== PROJECT PLAN DATA ======

project_name = "Sample Project Plan"

# Define tasks (task name, start date, end date)
tasks = [
    ("Requirement Analysis", "2025-01-01", "2025-01-05"),
    ("Design Phase", "2025-01-06", "2025-01-10"),
    ("Development Phase", "2025-01-11", "2025-01-20"),
    ("Testing Phase", "2025-01-21", "2025-01-27"),
    ("Deployment", "2025-01-28", "2025-01-30")
]

# Convert date strings to datetime objects
start_dates = [dt.datetime.strptime(t[1], "%Y-%m-%d") for t in tasks]
end_dates = [dt.datetime.strptime(t[2], "%Y-%m-%d") for t in tasks]

# Duration of each task
durations = [(end_dates[i] - start_dates[i]).days for i in range(len(tasks))]

# ====== GANTT CHART CREATION ======

fig, ax = plt.subplots(figsize=(10, 5))

for i, task in enumerate(tasks):
    ax.barh(task[0], durations[i], left=start_dates[i], height=0.4)

ax.set_title(project_name)
ax.set_xlabel("Timeline")
ax.set_ylabel("Tasks")

plt.tight_layout()
plt.show()


# --4 in powershell

# 	python gantt.py

	