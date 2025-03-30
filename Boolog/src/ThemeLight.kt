package hoodland.opensource.boolog

const val THEME_LIGHT = """
    <style>
        html {
            font-family: sans-serif
        }

        [class*='lvl-'] {
            display: none;
            cursor: auto;
        }

        input:checked~[class*='lvl-'] {
            display: block;
        }

        .gone {
            display: none;
        }

        .boolog {
            font-family: sans-serif;
            border-radius: 0.25em;
            border: 0.1em solid black;
            display: inline-block;
            background-color: #f1faff;
        }

        .failing_test_result {
            background-color: #ffb1a6;
        }

        .inconclusive_test_result {
            background-color: #fffbb9;
        }

        .passing_test_result {
            background-color: #c6ffad;
        }

        .implied_good {
            background-color: #e4ffd8;
        }

        .implied_caution {
            background-color: #fffdd8;
        }

        .implied_bad {
            background-color: #F6C6BD;
        }

        .neutral {
            background-color: #EBF4FA;
        }

        .old_parchment {
            background-color: #FFFFC2;
        }

        .plate {
            background-color: #DBE9FA;
        }

        .exception {
            background-image: linear-gradient(to bottom right, #fff2a6, #ffb0a6);
        }

        .decaf_green {
            background-color: #B0C6B2;
        }

        .decaf_orange {
            background-color: #EED886;
        }

        .decaf_green_light_roast {
            background-color: #DCE9DD;
        }

        .decaf_orange_light_roast {
            background-color: #F2E5B4;
        }


        table,
        th,
        td {
            padding: 0.1em 0em;
            margin-left: auto;
            margin-right: auto;
        }

        td.min {
            width: 1%;
            white-space: nowrap;
        }

        h1 {
            font-size: 3em;
            margin: 0em
        }

        h2 {
            font-size: 1.75em;
            margin: 0.2em
        }

        hr {
            border: none;
            height: 0.2em;
            background-color: black;
        }

        .centered {
            text-align: center;
        }

        .highlighted {
            background-color: yellow;
        }

        .outlined {
            display: inline-block;
            border-radius: 0.5em;
            border: 0.05em solid black;
            padding: 0.2em 0.2em;
        }

        .object {
            border-radius: 1.5em;
            border: 0.1em solid black;
            display: inline-block;
            padding: 0.4em 0.4em;
        }

        .incoming {
            border-radius: 3em 0.5em 0.5em 3em;
            border: 0.1em solid black;
            display: inline-block;
            padding: 1em 1em;
        }

        .outgoing {
            border-radius: 0.5em 3em 3em 0.5em;
            border: 0.1em solid black;
            display: inline-block;
            padding: 1em 1em;
        }

        .left_justified {
	        float: left;
        }

        table.gridlines,
        table.gridlines th,
        table.gridlines td {
            padding: 0.4em 0.4em;
            border-collapse: collapse;
            border: 0.02em solid black;
        }

        label {
            cursor: pointer;
        }
    </style>
    """