package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

object Repository {

    // --- Static Counties ---
    val firstClassCounties = listOf(
        "Lancashire", "Yorkshire", "Surrey", "Essex", "Somerset", "Warwickshire", "Nottinghamshire", "Middlesex"
    )

    // --- Mock Match Database ---
    private val _matches = MutableStateFlow<List<CricketMatch>>(emptyList())
    val matches: StateFlow<List<CricketMatch>> = _matches.asStateFlow()

    // --- Mock Players Database ---
    val playerDatabase = listOf(
        Player("p1", "James Anderson", "Right-arm fast-medium", 188, 1332, 10.3, "81", 54.1, 704, 26.4, "7/42", "Lancashire, England", false, "Modern"),
        Player("p2", "Joe Root", "Right-hand bat / Right-arm offbreak", 143, 11736, 49.3, "254", 72.8, 68, 44.5, "5/8", "Yorkshire, England", false, "Modern"),
        Player("p3", "Jack Hobbs", "Right-hand bat", 834, 61760, 50.7, "316*", 68.2, 108, 25.1, "7/56", "Surrey, England", true, "1905-1934"),
        Player("p4", "Fred Trueman", "Right-arm fast", 603, 9231, 14.8, "104", 45.4, 307, 21.5, "8/31", "Yorkshire, England", true, "1949-1968"),
        Player("p5", "Marcus Trescothick", "Left-hand bat", 342, 26342, 41.5, "260", 78.4, 20, 32.1, "3/15", "Somerset, England", true, "1993-2018"),
        Player("p6", "Brian Lara", "Left-hand bat", 299, 22156, 51.5, "501*", 84.1, 4, 104.0, "1/1", "Warwickshire, West Indies", true, "1990-2007"),
        Player("p7", "Alastair Cook", "Left-hand bat", 350, 26615, 46.8, "294", 48.2, 1, 107.0, "1/6", "Essex, England", true, "2003-2023"),
        Player("p8", "Ken Barrington", "Right-hand bat", 82, 6806, 58.7, "256", 42.1, 29, 44.8, "3/4", "Surrey, England", true, "1953-1968"),
        Player("p9", "Keaton Jennings", "Left-hand bat", 152, 9820, 39.1, "318", 49.6, 12, 110.0, "2/14", "Lancashire, England", false, "Modern"),
        Player("p10", "Harry Brook", "Right-hand bat", 68, 4820, 48.5, "186", 92.4, 2, 85.0, "1/25", "Yorkshire, England", false, "Modern")
    )

    // --- Mock Grounds Database ---
    val groundDatabase = listOf(
        Venue(
            "lords", "Lord's Cricket Ground", "London",
            "Established in 1814 by Thomas Lord. Known globally as the 'Home of Cricket', housing the iconic grade II-listed Lord's Pavilion which dates back to 1890. It is the headquarters of the Marylebone Cricket Club (MCC).",
            "31,100",
            "Highest Score: 729/6 dec (Australia, 1930)\nLowest Score: 42 (Ireland, 2019)\nMost Wickets: James Anderson (117)",
            "Scattered Clouds", "21°C"
        ),
        Venue(
            "oldtrafford", "Emirates Old Trafford", "Manchester",
            "Home of Lancashire County Cricket Club since 1864. Famous for being one of the oldest test venues in England, hosting the birthplace of the legendary 1902 Test where England won by 3 runs.",
            "26,000",
            "Highest Score: 656/8 dec (England, 1964)\nLowest Score: 58 (India, 1952)\nMost Runs: Denis Compton (1,211)",
            "Passing Showers", "17°C"
        ),
        Venue(
            "oval", "The Kia Oval", "London",
            "Home of Surrey County Cricket Club since 1845. This historic ground hosted the first ever Test match in England in September 1880, and famously gave birth to the Ashes after Australia defeated England in 1882.",
            "27,500",
            "Highest Score: 903/7 dec (England, 1938)\nLowest Score: 44 (England, 1896)\nMost Centuries: Jack Hobbs (11)",
            "Sunny Intervals", "22°C"
        ),
        Venue(
            "headingley", "Headingley Cricket Ground", "Leeds",
            "Home of Yorkshire County Cricket Club since 1890. Scene of some of the greatest finishes in Test history, notably Ian Botham's Ashes miracle in 1981 and Ben Stokes' historic match-winning century in 2019.",
            "18,300",
            "Highest Score: 653/4 dec (Australia, 1934)\nLowest Score: 61 (West Indies, 1969)\nMost Runs: Don Bradman (963)",
            "Overcast", "16°C"
        ),
        Venue(
            "edgbaston", "Edgbaston Stadium", "Birmingham",
            "Home of Warwickshire County Cricket Club. Famed for its vocal atmosphere (especially the Eric Hollies Stand). It hosted the epic 2-run Ashes finish in 2005, one of the greatest matches ever recorded.",
            "25,000",
            "Highest Score: 710/7 dec (England, 2011)\nLowest Score: 30 (South Africa, 1924)",
            "Clear & Bright", "20°C"
        ),
        Venue(
            "trentbridge", "Trent Bridge", "Nottingham",
            "Home of Nottinghamshire, first established in 1841. Famous for its pristine outfield, historic pavilion, and unique atmospheric conditions which make it a legendary paradise for swing bowlers worldwide.",
            "17,500",
            "Highest Score: 658/8 (England, 1938)\nLowest Score: 60 (Australia, 2015)\nMost Wickets: Stuart Broad (42)",
            "Sunny", "21°C"
        )
    )

    // --- Mock Newsroom ---
    val newsArticles = listOf(
        NewsArticle("n1", "Jennings Century Guides Red Rose Command at Old Trafford", "County", "Keaton Jennings constructed a masterful 142 on day two as Lancashire laid a firm foundation against Surrey in the County Championship.", "Keaton Jennings batted for over six hours, defying a potent Surrey attack led by Kemar Roach, to compile a classical 142. His century anchor-rolled Lancashire to 388 all out, putting the hosts in command at Old Trafford. Combined with a brisk fifty from George Balderson, Lancashire controlled the afternoon session with rail-timetable precision. Surrey will face a testing evening burst under heavy clouds.", "2 hours ago", "Wisden Correspondent"),
        NewsArticle("n2", "Lord's 2050: MCC Unveils Next-Gen Canopy Design Blueprint", "International", "The Marylebone Cricket Club has unveiled futuristic blueprints featuring a retractable carbon-fibre weather canopy at the Home of Cricket.", "In an extraordinary press release, the MCC revealed details of a Lord's 2050 design proposal. The masterplan includes an advanced retractable smart canopy that monitors cloud formations and deploys silently in under 45 seconds to eliminate rain interruptions. Rooted in traditional Lord's Pavilion geometry, this represents a major step towards the future. Club secretary remarked, 'The game is larger than any individual match. We must secure its digital and physical future.'", "5 hours ago", "Anglo-Futurist Bureau"),
        NewsArticle("n3", "Under 19 County League Flourishes: Grassroots Cricket Reports High Engagement", "Grassroots", "Local community clubs report record participation in the digitized youth league, boosting junior retention rates.", "The grassroots platform of Cricket House has seen staggering adoption. Over 450 clubs in Lancashire, Yorkshire, and Yorkshire leagues have digitized their scoring overlays, leading to live-stream telemetry of the youngest talents. These metrics connect local village greens straight into the larger National Cricket Archive.", "1 day ago", "Club House Desk")
    )

    // --- Historical Archives ---
    val historicalArchive = listOf(
        HistoricalArchiveItem("h1", "The Birth of the Ashes (1882)", "1882", "Following Australia's first-ever Test victory on English soil at The Oval, the Sporting Times published a satirical obituary of English cricket, stating that the body would be cremated and the ashes taken to Australia.", "England 101 & 77 lost to Australia 63 & 122 by 7 runs. Fred Spofforth claimed 14 wickets in the match."),
        HistoricalArchiveItem("h2", "Lara's Historic 501* at Edgbaston (1994)", "1994", "Brian Lara shattered the world record for the highest individual score in first-class cricket, scoring 501 not out for Warwickshire against Durham in just 427 deliveries.", "Warwickshire 810/4 dec. Lara hit 62 fours and 10 sixes with a strike rate of 117.3."),
        HistoricalArchiveItem("h3", "Botham's Miracle at Headingley (1981)", "1981", "Following on and facing 500-1 bookmakers' odds, Ian Botham's swashbuckling 149 not out turned the match and series on its head, snatching victory from Australia.", "England 174 & 356/9 dec defeated Australia 401/9 dec & 111 by 18 runs. Bob Willis took 8/43.")
    )

    // --- Grasroots Platform Leagues ---
    val clubLeagues = listOf(
        "Pennine Cricket League", "Surrey Championship Top Tier", "Yorkshire Premier Sward", "Somerset Major Division"
    )
    val clubTeams = listOf(
        "Clifton CC", "Ramsbottom CC", "Guildford CC", "Wimbledon CC", "York CC", "Taunton St Andrews CC"
    )

    init {
        // Hydrate default live and completed matches
        _matches.value = listOf(
            CricketMatch(
                id = "m1",
                teamA = "Lancashire",
                teamB = "Surrey",
                scoreA = "388 & 162/2",
                scoreB = "294",
                oversA = "45.2",
                oversB = "98.4",
                teamABatting = true,
                matchStatus = "Lancashire lead by 256 runs (Day 3)",
                venue = "Emirates Old Trafford",
                type = "County Championship",
                runRate = "3.58",
                partnership = "84 runs (124 balls)",
                winProbabilityA = 68,
                winProbabilityB = 12,
                isLive = true,
                batsman1 = "K. Jennings",
                batsman1Runs = 84,
                batsman1Balls = 135,
                batsman2 = "J. Bohannon",
                batsman2Runs = 41,
                batsman2Balls = 88,
                bowler = "K. Roach",
                bowlerOvers = 14.0,
                bowlerRuns = 42,
                bowlerWickets = 1,
                ballByBallCommentary = listOf(
                    "45.2 - K. Roach to J. Bohannon, 1 run, tucked off his pads to deep backward square leg.",
                    "45.1 - K. Roach to K. Jennings, 1 run, driven firmly past the non-striker for a single.",
                    "44.6 - D. Worrall to J. Bohannon, no run, solid forward defensive block.",
                    "44.5 - D. Worrall to J. Bohannon, FOUR, beautiful cover drive cracked through the gap with immaculate timing!",
                    "44.4 - D. Worrall to K. Jennings, 1 run, worked gently into the leg side gap.",
                    "44.3 - D. Worrall to K. Jennings, no run, beaten! Lovely outswinger landing on a length, Jennings pokes and misses."
                )
            ),
            CricketMatch(
                id = "m2",
                teamA = "Yorkshire",
                teamB = "Somerset",
                scoreA = "242 & 118/4",
                scoreB = "310",
                oversA = "34.0",
                oversB = "102.5",
                teamABatting = true,
                matchStatus = "Yorkshire trail by 150 runs",
                venue = "Headingley",
                type = "County Championship",
                runRate = "3.47",
                partnership = "12 runs (22 balls)",
                winProbabilityA = 32,
                winProbabilityB = 54,
                isLive = true,
                batsman1 = "H. Brook",
                batsman1Runs = 18,
                batsman1Balls = 24,
                batsman2 = "A. Lyth",
                batsman2Runs = 62,
                batsman2Balls = 98,
                bowler = "J. Davey",
                bowlerOvers = 11.2,
                bowlerRuns = 31,
                bowlerWickets = 2,
                ballByBallCommentary = listOf(
                    "34.0 - J. Davey to H. Brook, FOUR, short ball dispatched through mid-wicket with an autoritative pull!",
                    "33.5 - J. Davey to A. Lyth, 1 run, back of a length worked off the hips."
                )
            ),
            CricketMatch(
                id = "m3",
                teamA = "Warwickshire",
                teamB = "Essex",
                scoreA = "165/1",
                scoreB = "162",
                oversA = "18.3",
                oversB = "20.0",
                teamABatting = true,
                matchStatus = "Warwickshire won by 9 wickets",
                venue = "Edgbaston",
                type = "T20 Blast",
                runRate = "8.91",
                partnership = "94 runs (52 balls)",
                winProbabilityA = 100,
                winProbabilityB = 0,
                isLive = false,
                batsman1 = "M. Mousley",
                batsman1Runs = 76,
                batsman1Balls = 45,
                batsman2 = "A. Davies",
                batsman2Runs = 62,
                batsman2Balls = 38,
                bowler = "S. Harmer",
                bowlerOvers = 4.0,
                bowlerRuns = 42,
                bowlerWickets = 0,
                ballByBallCommentary = listOf(
                    "18.3 - S. Harmer to M. Mousley, SIX, high over long-on! That seals the victory for Warwickshire in spectacular style!"
                )
            ),
            CricketMatch(
                id = "m4",
                teamA = "Wimbledon CC",
                teamB = "Guildford CC",
                scoreA = "215/6",
                scoreB = "118/3",
                oversA = "45.0",
                oversB = "25.2",
                teamABatting = false,
                matchStatus = "Guildford CC need 98 runs in 19.4 overs",
                venue = "Wimbledon Common Ground",
                type = "Club Cricket",
                runRate = "4.65",
                partnership = "32 runs (45 balls)",
                winProbabilityA = 55,
                winProbabilityB = 45,
                isLive = true,
                batsman1 = "T. Harrison",
                batsman1Runs = 45,
                batsman1Balls = 62,
                batsman2 = "G. Cole",
                batsman2Runs = 23,
                batsman2Balls = 30,
                bowler = "A. Fletcher",
                bowlerOvers = 5.2,
                bowlerRuns = 22,
                bowlerWickets = 1,
                ballByBallCommentary = listOf(
                    "25.2 - A. Fletcher to T. Harrison, 1 run, pushed into the gap at cover."
                )
            )
        )
    }

    // --- Simulated Real-time Match Updates ---
    fun simulateScoringFeed(): Flow<CricketMatch> = flow {
        while (true) {
            kotlinx.coroutines.delay(10000) // update every 10 seconds
            val currentList = _matches.value.toMutableList()
            for (i in currentList.indices) {
                val match = currentList[i]
                if (match.isLive) {
                    val runChance = Random.nextInt(100)
                    val newBatsman1Runs: Int
                    val newBatsman1Balls = match.batsman1Balls + 1
                    var ballEvent = ""
                    val currentOverParts = match.oversA.split(".")
                    var overs = currentOverParts[0].toInt()
                    var balls = if (currentOverParts.size > 1) currentOverParts[1].toInt() else 0
                    balls++
                    if (balls >= 6) {
                        balls = 0
                        overs++
                    }
                    val newOvers = "$overs.$balls"

                    val scoreParts = match.scoreA.split(" & ")
                    var beforeRuns = ""
                    var currentInningsStr = scoreParts.last()
                    if (scoreParts.size > 1) {
                        beforeRuns = scoreParts[0] + " & "
                    }
                    val runParts = currentInningsStr.split("/")
                    var runs = runParts[0].toInt()
                    var wickets = if (runParts.size > 1) runParts[1].toInt() else 0

                    if (runChance < 5) {
                        // wicket!
                        wickets++
                        newBatsman1Runs = 0
                        val randomNames = listOf("T. Hartley", "L. Wells", "P. Chawla", "D. Mitchell")
                        val newBatsman = randomNames.random()
                        ballEvent = "WICKET! ${match.batsman1} is caught at slip! Gone for ${match.batsman1Runs}. Welcome new batsman $newBatsman."
                        currentList[i] = match.copy(
                            scoreA = "$beforeRuns$runs/$wickets",
                            oversA = newOvers,
                            batsman1 = newNewBatsman(match.batsman1, newBatsman),
                            batsman1Runs = 0,
                            batsman1Balls = 0,
                            bowlerWickets = match.bowlerWickets + 1,
                            bowlerOvers = match.bowlerOvers + 0.1,
                            ballByBallCommentary = listOf("$newOvers - ${match.bowler} to ${match.batsman1}, OUT! $ballEvent") + match.ballByBallCommentary.take(9)
                        )
                    } else {
                        // scoring runs
                        val runsScored = when {
                            runChance < 40 -> 0
                            runChance < 75 -> 1
                            runChance < 85 -> 2
                            runChance < 95 -> 4
                            else -> 6
                        }
                        runs += runsScored
                        newBatsman1Runs = match.batsman1Runs + runsScored
                        val runsStr = if (runsScored > 0) "$runsScored run" else "no run"
                        val runsCaps = if (runsScored == 4) "FOUR! Beautifully timed" else if (runsScored == 6) "SIX! Massive strike into the crowd!" else runsStr
                        ballEvent = "${match.bowler} to ${match.batsman1}, $runsCaps."

                        currentList[i] = match.copy(
                            scoreA = "$beforeRuns$runs/$wickets",
                            oversA = newOvers,
                            batsman1Runs = newBatsman1Runs,
                            batsman1Balls = newBatsman1Balls,
                            bowlerRuns = match.bowlerRuns + runsScored,
                            bowlerOvers = if (balls == 0) match.bowlerOvers + 0.5 else match.bowlerOvers + 0.1,
                            ballByBallCommentary = listOf("$newOvers - $ballEvent") + match.ballByBallCommentary.take(9)
                        )
                    }
                }
            }
            _matches.value = currentList
            emit(currentList.first())
        }
    }

    private fun newNewBatsman(oldName: String, nameSuggestion: String): String {
        return nameSuggestion
    }
}
