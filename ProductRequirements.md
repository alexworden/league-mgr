## Instructions for Building the Pool League Web Application

### Tech Stack

- **Frontend**: React (with TypeScript recommended for better type safety).
- **Backend**: Java Spring Boot.
- **Database**: PostgreSQL.
- **Data Access**: jOOQ for database interactions (no ORM).

---

### Phase 1: User Account and Pool League Management

#### User Account Creation

1. Allow users to sign up using their email addresses.
2. Implement user authentication using JWT for secure sessions.
3. Use a `users` table to store:
   - `id` (primary key)
   - `email` (unique)
   - `password_hash`
   - `created_at`
   - `updated_at`

#### Pool League Creation

1. Allow registered users to create a new pool league.
2. When creating a pool league:
   - Prompt the user to name the league.
   - Register the user as the administrator of the league.
3. Use a `pool_leagues` table to store:
   - `id` (primary key)
   - `name`
   - `admin_user_id` (foreign key to `users`)
   - `created_at`
   - `updated_at`
4. Use a `league_memberships` table to store:
   - `id` (primary key)
   - `pool_league_id` (foreign key to `pool_leagues`)
   - `user_id` (foreign key to `users`)
   - `role` (e.g., `admin`, `member`)
   - `joined_at`

#### Dashboard

1. Create a dashboard for users to view:
   - Pool leagues they manage.
   - Pool leagues they are members of.
2. Implement APIs to fetch relevant data for the dashboard.

#### Player Management

1. Allow administrators to:
   - Add players directly by email.
   - Generate and share unique URLs for league invitations.
2. Invitation workflow:
   - Generate a URL with a unique token.
   - Store the token in an `invitations` table with:
     - `id` (primary key)
     - `pool_league_id` (foreign key to `pool_leagues`)
     - `token` (unique)
     - `created_at`
     - `expires_at`
   - When a user visits the URL, verify the token and allow sign-up/sign-in and automatic league membership.

---

### Phase 2: Team Management

#### Manual Team Organization

1. Allow administrators to manually organize players into teams.
2. Use a `teams` table to store:
   - `id` (primary key)
   - `pool_league_id` (foreign key to `pool_leagues`)
   - `name`
   - `created_at`
3. Use a `team_memberships` table to store:
   - `id` (primary key)
   - `team_id` (foreign key to `teams`)
   - `user_id` (foreign key to `users`)
   - `joined_at`

#### Automatic Team Organization

1. Allow administrators to set the number of players per team (default: 3).
2. Randomly assign players to teams, ensuring even distribution.
3. Validate team sizes before finalizing.

---

### Phase 3: Scheduling

#### Configuring the Schedule

1. Allow administrators to:
   - Define which days of the week games will be played (e.g., weekly on Sundays).
   - Set the number of games that can be played per session (day). 
2. Store schedule configurations in a `schedules` table:
   - `id` (primary key)
   - `pool_league_id` (foreign key to `pool_leagues`)
   - `frequency` (e.g., `daily`, `weekly`)
   - `day_of_week` (if weekly)
   - `games_per_session`

#### Automated Scheduling

1. Generate a schedule based on league settings.
2. Store games in a `games` table:
   - `id` (primary key)
   - `pool_league_id` (foreign key to `pool_leagues`)
   - `team1_id` (foreign key to `teams`)
   - `team2_id` (foreign key to `teams`)
   - `scheduled_at`
   - `team1_score` (nullable for games not yet played)
   - `team2_score` (nullable for games not yet played)
   - `winner_team_id` (nullable for games not yet played)
   - `player_scores` (JSON field to store individual player scores within the context of the game, e.g., `{player1_id: 5, player2_id: 3}`)
3. Allow editing of the schedule. It should be possible to edit the games that are scheduled and/or the players playing but the tool should help ensure that the necessary teams have games that are played and that players have played the necessary amount of games during a full competition. It must be flexible to allow for when some teams and/or players cannot play on a particular day's session. 

---

### Phase 4: Game Results and Tracking

#### Viewing Games

1. Create a page to allow players to do the following: 
   - See the pool leagues they’re participating in. This should be very user friendly and select the currently active / upcoming league automatically if there is only one, otherwise show a list of current or upcoming leagues. 
   - View a calendar or list of past and upcoming games.
   - Select games they are scheduled to play in.

#### Recording Results

1. Allow teams to record game results:
   - Retroactively or after the game concludes.
2. Use the `games` table to store results:
   - Update `team1_score` and `team2_score` fields for recorded games.
   - Add individual player scores into the `player_scores` JSON field within the same record.
3. Calculate team and individual scores dynamically by querying games with recorded results.
4. Allow administrators to edit results if corrections are needed.

---

### Phase 5: Visualizing Competition Progress

#### Leaderboards

1. Display a leaderboard with:
   - Teams ranked by total points.
   - Individual players ranked by their total scores across games.
   - Scores updated dynamically as games are recorded.
2. Create a graphical view of league progress using charts.

---

### Frontend Enhancements for UI/UX

#### Dynamic Dashboards

1. Implement interactive charts and graphs (e.g., Chart.js or D3.js) to visualize competition progress.
2. Use a card-based layout to summarize key metrics like upcoming games, player rankings, and team scores.

#### Responsive Design

1. Utilize CSS frameworks such as Tailwind CSS or Material-UI for responsive and modern designs.
2. Ensure seamless functionality on desktops, tablets, and mobile devices.

#### Themes and Accessibility

1. Add light and dark themes with an easy toggle for users.
2. Ensure accessibility by adhering to WCAG 2.1 standards.
3. Use semantic HTML and ARIA attributes where necessary to support screen readers.
4. Design for readability with:
   - Larger font sizes for text (minimum 16px, adjustable for headings and body text).
   - High-contrast color schemes to aid users with poor near-vision.

#### Mobile Optimization

1. Prioritize mobile usability for player views, ensuring that:
   - All key features (e.g., game schedule, results entry, leaderboards) are easily accessible on smaller screens.
   - Buttons and interactive elements are large enough for easy tapping.

#### Navigation

1. Implement an intuitive navigation system with clear labels and breadcrumb trails for better user experience.
2. Add a fixed header with quick access to frequently used features like dashboards, schedules, and results.

---

### Folder Structure

#### Backend

- **`src/main/java`**:
  - `controllers`: Handle HTTP requests.
  - `services`: Business logic.
  - `repositories`: jOOQ for database access.
  - `models`: Data types.

#### Frontend

- **`src`**:
  - `components`: Reusable React components.
  - `pages`: Page-level components.
  - `api`: API calls.
  - `types`: TypeScript type definitions.
  - `utils`: Utility functions.

---

### Deployment

1. Use Railway.app will be used for hosting from GitHub. For a first pass, just aim to have the web application deployable locally and available locally.

