<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Wordle Hint Service</title>
</head>
<body>
<div align="left">
<h1>Let's solve some Wordles!</h1>
    <form action="getHint" method="post">
        <h3>
            Zack recommends a first guess of "AEROS" or "UNTIL". <br> <br>
            To enter the results of a guess, refer to this legend:
            B = black tile, Y = yellow tile, G = green tile <br>
            So if your guess was "AEROS" and the "A" is yellow, "O" is green, and rest are black,
            your input would be "YBBGB" <br> <br>
            For guesses that you have not reached yet, leave the box empty.
        </h3>
        <p>
            Enter your guess #1 here <input type="text" name="guess1" required />
        </p>
        <p>
            Enter the results of guess #1 here <input type="text" name="result1" required />
        </p>
        <p>
            Enter your guess #2 here <input type="text" name="guess2" />
        </p>
        <p>
            Enter the results of guess #2 here <input type="text" name="result2" />
        </p>
        <p>
            Enter your guess #3 here <input type="text" name="guess3" />
        </p>
        <p>
            Enter the results of guess #3 here <input type="text" name="result3" />
        </p>
        <p>
            Enter your guess #4 here <input type="text" name="guess4" />
        </p>
        <p>
            Enter the results of guess #4 here <input type="text" name="result4" />
        </p>
        <p>
            Enter your guess #5 here <input type="text" name="guess5" />
        </p>
        <p>
            Enter the results of guess #5 here <input type="text" name="result5" />
        </p>
        <p>
           <input type="submit" value="Get hint"/>
        </p>
    </form>

</div>
</body>
</html>