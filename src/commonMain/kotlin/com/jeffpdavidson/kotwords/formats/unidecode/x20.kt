package com.jeffpdavidson.kotwords.formats.unidecode

internal val x20 = arrayOf(
    // 0x2000:   =>  
    "\u0020",
    // 0x2001:   =>  
    "\u0020",
    // 0x2002:   =>  
    "\u0020",
    // 0x2003:   =>  
    "\u0020",
    // 0x2004:   =>  
    "\u0020",
    // 0x2005:   =>  
    "\u0020",
    // 0x2006:   =>  
    "\u0020",
    // 0x2007:   =>  
    "\u0020",
    // 0x2008:   =>  
    "\u0020",
    // 0x2009:   =>  
    "\u0020",
    // 0x200a:   =>  
    "\u0020",
    // 0x200b: ​ =>  
    "\u0020",
    // 0x200c: ‌ => 
    "",
    // 0x200d: ‍ => 
    "",
    // 0x200e: ‎ => 
    "",
    // 0x200f: ‏ => 
    "",
    // 0x2010: ‐ => -
    "\u002d",
    // 0x2011: ‑ => -
    "\u002d",
    // 0x2012: ‒ => -
    "\u002d",
    // 0x2013: – => -
    "\u002d",
    // 0x2014: — => --
    "\u002d\u002d",
    // 0x2015: ― => --
    "\u002d\u002d",
    // 0x2016: ‖ => ||
    "\u007c\u007c",
    // 0x2017: ‗ => _
    "\u005f",
    // 0x2018: ‘ => '
    "\u0027",
    // 0x2019: ’ => '
    "\u0027",
    // 0x201a: ‚ => ,
    "\u002c",
    // 0x201b: ‛ => '
    "\u0027",
    // 0x201c: “ => "
    "\u0022",
    // 0x201d: ” => "
    "\u0022",
    // 0x201e: „ => ,,
    "\u002c\u002c",
    // 0x201f: ‟ => "
    "\u0022",
    // 0x2020: † => +
    "\u002b",
    // 0x2021: ‡ => ++
    "\u002b\u002b",
    // 0x2022: • => *
    "\u002a",
    // 0x2023: ‣ => *>
    "\u002a\u003e",
    // 0x2024: ․ => .
    "\u002e",
    // 0x2025: ‥ => ..
    "\u002e\u002e",
    // 0x2026: … => ...
    "\u002e\u002e\u002e",
    // 0x2027: ‧ => .
    "\u002e",
    // 0x2028:   => 

    "\u000a",
    // 0x2029:   => 


    "\u000a\u000a",
    // 0x202a: ‪ => 
    "",
    // 0x202b: ‫ => 
    "",
    // 0x202c: ‬ => 
    "",
    // 0x202d: ‭ => 
    "",
    // 0x202e: ‮ => 
    "",
    // 0x202f:   =>  
    "\u0020",
    // 0x2030: ‰ => %0
    "\u0025\u0030",
    // 0x2031: ‱ => %00
    "\u0025\u0030\u0030",
    // 0x2032: ′ => '
    "\u0027",
    // 0x2033: ″ => ''
    "\u0027\u0027",
    // 0x2034: ‴ => '''
    "\u0027\u0027\u0027",
    // 0x2035: ‵ => `
    "\u0060",
    // 0x2036: ‶ => ``
    "\u0060\u0060",
    // 0x2037: ‷ => ```
    "\u0060\u0060\u0060",
    // 0x2038: ‸ => ^
    "\u005e",
    // 0x2039: ‹ => <
    "\u003c",
    // 0x203a: › => >
    "\u003e",
    // 0x203b: ※ => *
    "\u002a",
    // 0x203c: ‼ => !!
    "\u0021\u0021",
    // 0x203d: ‽ => !?
    "\u0021\u003f",
    // 0x203e: ‾ => -
    "\u002d",
    // 0x203f: ‿ => _
    "\u005f",
    // 0x2040: ⁀ => -
    "\u002d",
    // 0x2041: ⁁ => ^
    "\u005e",
    // 0x2042: ⁂ => ***
    "\u002a\u002a\u002a",
    // 0x2043: ⁃ => --
    "\u002d\u002d",
    // 0x2044: ⁄ => /
    "\u002f",
    // 0x2045: ⁅ => -[
    "\u002d\u005b",
    // 0x2046: ⁆ => ]-
    "\u005d\u002d",
    // 0x2047: ⁇ => [?]
    "\u005b\u003f\u005d",
    // 0x2048: ⁈ => ?!
    "\u003f\u0021",
    // 0x2049: ⁉ => !?
    "\u0021\u003f",
    // 0x204a: ⁊ => 7
    "\u0037",
    // 0x204b: ⁋ => PP
    "\u0050\u0050",
    // 0x204c: ⁌ => (]
    "\u0028\u005d",
    // 0x204d: ⁍ => [)
    "\u005b\u0029",
    // 0x204e: ⁎ => [?]
    "\u005b\u003f\u005d",
    // 0x204f: ⁏ => [?]
    "\u005b\u003f\u005d",
    // 0x2050: ⁐ => [?]
    "\u005b\u003f\u005d",
    // 0x2051: ⁑ => [?]
    "\u005b\u003f\u005d",
    // 0x2052: ⁒ => [?]
    "\u005b\u003f\u005d",
    // 0x2053: ⁓ => [?]
    "\u005b\u003f\u005d",
    // 0x2054: ⁔ => [?]
    "\u005b\u003f\u005d",
    // 0x2055: ⁕ => [?]
    "\u005b\u003f\u005d",
    // 0x2056: ⁖ => [?]
    "\u005b\u003f\u005d",
    // 0x2057: ⁗ => [?]
    "\u005b\u003f\u005d",
    // 0x2058: ⁘ => [?]
    "\u005b\u003f\u005d",
    // 0x2059: ⁙ => [?]
    "\u005b\u003f\u005d",
    // 0x205a: ⁚ => [?]
    "\u005b\u003f\u005d",
    // 0x205b: ⁛ => [?]
    "\u005b\u003f\u005d",
    // 0x205c: ⁜ => [?]
    "\u005b\u003f\u005d",
    // 0x205d: ⁝ => [?]
    "\u005b\u003f\u005d",
    // 0x205e: ⁞ => [?]
    "\u005b\u003f\u005d",
    // 0x205f:   => [?]
    "\u005b\u003f\u005d",
    // 0x2060: ⁠ => [?]
    "\u005b\u003f\u005d",
    // 0x2061: ⁡ => [?]
    "\u005b\u003f\u005d",
    // 0x2062: ⁢ => [?]
    "\u005b\u003f\u005d",
    // 0x2063: ⁣ => [?]
    "\u005b\u003f\u005d",
    // 0x2064: ⁤ => [?]
    "\u005b\u003f\u005d",
    // 0x2065: ⁥ => [?]
    "\u005b\u003f\u005d",
    // 0x2066: ⁦ => [?]
    "\u005b\u003f\u005d",
    // 0x2067: ⁧ => [?]
    "\u005b\u003f\u005d",
    // 0x2068: ⁨ => [?]
    "\u005b\u003f\u005d",
    // 0x2069: ⁩ => [?]
    "\u005b\u003f\u005d",
    // 0x206a: ⁪ => 
    "",
    // 0x206b: ⁫ => 
    "",
    // 0x206c: ⁬ => 
    "",
    // 0x206d: ⁭ => 
    "",
    // 0x206e: ⁮ => 
    "",
    // 0x206f: ⁯ => 
    "",
    // 0x2070: ⁰ => 0
    "\u0030",
    // 0x2071: ⁱ => 
    "",
    // 0x2072: ⁲ => 
    "",
    // 0x2073: ⁳ => 
    "",
    // 0x2074: ⁴ => 4
    "\u0034",
    // 0x2075: ⁵ => 5
    "\u0035",
    // 0x2076: ⁶ => 6
    "\u0036",
    // 0x2077: ⁷ => 7
    "\u0037",
    // 0x2078: ⁸ => 8
    "\u0038",
    // 0x2079: ⁹ => 9
    "\u0039",
    // 0x207a: ⁺ => +
    "\u002b",
    // 0x207b: ⁻ => -
    "\u002d",
    // 0x207c: ⁼ => =
    "\u003d",
    // 0x207d: ⁽ => (
    "\u0028",
    // 0x207e: ⁾ => )
    "\u0029",
    // 0x207f: ⁿ => n
    "\u006e",
    // 0x2080: ₀ => 0
    "\u0030",
    // 0x2081: ₁ => 1
    "\u0031",
    // 0x2082: ₂ => 2
    "\u0032",
    // 0x2083: ₃ => 3
    "\u0033",
    // 0x2084: ₄ => 4
    "\u0034",
    // 0x2085: ₅ => 5
    "\u0035",
    // 0x2086: ₆ => 6
    "\u0036",
    // 0x2087: ₇ => 7
    "\u0037",
    // 0x2088: ₈ => 8
    "\u0038",
    // 0x2089: ₉ => 9
    "\u0039",
    // 0x208a: ₊ => +
    "\u002b",
    // 0x208b: ₋ => -
    "\u002d",
    // 0x208c: ₌ => =
    "\u003d",
    // 0x208d: ₍ => (
    "\u0028",
    // 0x208e: ₎ => )
    "\u0029",
    // 0x208f: ₏ => [?]
    "\u005b\u003f\u005d",
    // 0x2090: ₐ => [?]
    "\u005b\u003f\u005d",
    // 0x2091: ₑ => [?]
    "\u005b\u003f\u005d",
    // 0x2092: ₒ => [?]
    "\u005b\u003f\u005d",
    // 0x2093: ₓ => [?]
    "\u005b\u003f\u005d",
    // 0x2094: ₔ => [?]
    "\u005b\u003f\u005d",
    // 0x2095: ₕ => [?]
    "\u005b\u003f\u005d",
    // 0x2096: ₖ => [?]
    "\u005b\u003f\u005d",
    // 0x2097: ₗ => [?]
    "\u005b\u003f\u005d",
    // 0x2098: ₘ => [?]
    "\u005b\u003f\u005d",
    // 0x2099: ₙ => [?]
    "\u005b\u003f\u005d",
    // 0x209a: ₚ => [?]
    "\u005b\u003f\u005d",
    // 0x209b: ₛ => [?]
    "\u005b\u003f\u005d",
    // 0x209c: ₜ => [?]
    "\u005b\u003f\u005d",
    // 0x209d: ₝ => [?]
    "\u005b\u003f\u005d",
    // 0x209e: ₞ => [?]
    "\u005b\u003f\u005d",
    // 0x209f: ₟ => [?]
    "\u005b\u003f\u005d",
    // 0x20a0: ₠ => ECU
    "\u0045\u0043\u0055",
    // 0x20a1: ₡ => CL
    "\u0043\u004c",
    // 0x20a2: ₢ => Cr
    "\u0043\u0072",
    // 0x20a3: ₣ => FF
    "\u0046\u0046",
    // 0x20a4: ₤ => L
    "\u004c",
    // 0x20a5: ₥ => mil
    "\u006d\u0069\u006c",
    // 0x20a6: ₦ => N
    "\u004e",
    // 0x20a7: ₧ => Pts
    "\u0050\u0074\u0073",
    // 0x20a8: ₨ => Rs
    "\u0052\u0073",
    // 0x20a9: ₩ => W
    "\u0057",
    // 0x20aa: ₪ => NS
    "\u004e\u0053",
    // 0x20ab: ₫ => D
    "\u0044",
    // 0x20ac: € => EUR
    "\u0045\u0055\u0052",
    // 0x20ad: ₭ => K
    "\u004b",
    // 0x20ae: ₮ => T
    "\u0054",
    // 0x20af: ₯ => Dr
    "\u0044\u0072",
    // 0x20b0: ₰ => [?]
    "\u005b\u003f\u005d",
    // 0x20b1: ₱ => [?]
    "\u005b\u003f\u005d",
    // 0x20b2: ₲ => [?]
    "\u005b\u003f\u005d",
    // 0x20b3: ₳ => [?]
    "\u005b\u003f\u005d",
    // 0x20b4: ₴ => [?]
    "\u005b\u003f\u005d",
    // 0x20b5: ₵ => [?]
    "\u005b\u003f\u005d",
    // 0x20b6: ₶ => [?]
    "\u005b\u003f\u005d",
    // 0x20b7: ₷ => [?]
    "\u005b\u003f\u005d",
    // 0x20b8: ₸ => [?]
    "\u005b\u003f\u005d",
    // 0x20b9: ₹ => [?]
    "\u005b\u003f\u005d",
    // 0x20ba: ₺ => [?]
    "\u005b\u003f\u005d",
    // 0x20bb: ₻ => [?]
    "\u005b\u003f\u005d",
    // 0x20bc: ₼ => [?]
    "\u005b\u003f\u005d",
    // 0x20bd: ₽ => [?]
    "\u005b\u003f\u005d",
    // 0x20be: ₾ => [?]
    "\u005b\u003f\u005d",
    // 0x20bf: ₿ => [?]
    "\u005b\u003f\u005d",
    // 0x20c0: ⃀ => [?]
    "\u005b\u003f\u005d",
    // 0x20c1: ⃁ => [?]
    "\u005b\u003f\u005d",
    // 0x20c2: ⃂ => [?]
    "\u005b\u003f\u005d",
    // 0x20c3: ⃃ => [?]
    "\u005b\u003f\u005d",
    // 0x20c4: ⃄ => [?]
    "\u005b\u003f\u005d",
    // 0x20c5: ⃅ => [?]
    "\u005b\u003f\u005d",
    // 0x20c6: ⃆ => [?]
    "\u005b\u003f\u005d",
    // 0x20c7: ⃇ => [?]
    "\u005b\u003f\u005d",
    // 0x20c8: ⃈ => [?]
    "\u005b\u003f\u005d",
    // 0x20c9: ⃉ => [?]
    "\u005b\u003f\u005d",
    // 0x20ca: ⃊ => [?]
    "\u005b\u003f\u005d",
    // 0x20cb: ⃋ => [?]
    "\u005b\u003f\u005d",
    // 0x20cc: ⃌ => [?]
    "\u005b\u003f\u005d",
    // 0x20cd: ⃍ => [?]
    "\u005b\u003f\u005d",
    // 0x20ce: ⃎ => [?]
    "\u005b\u003f\u005d",
    // 0x20cf: ⃏ => [?]
    "\u005b\u003f\u005d",
    // 0x20d0: ⃐ => 
    "",
    // 0x20d1: ⃑ => 
    "",
    // 0x20d2: ⃒ => 
    "",
    // 0x20d3: ⃓ => 
    "",
    // 0x20d4: ⃔ => 
    "",
    // 0x20d5: ⃕ => 
    "",
    // 0x20d6: ⃖ => 
    "",
    // 0x20d7: ⃗ => 
    "",
    // 0x20d8: ⃘ => 
    "",
    // 0x20d9: ⃙ => 
    "",
    // 0x20da: ⃚ => 
    "",
    // 0x20db: ⃛ => 
    "",
    // 0x20dc: ⃜ => 
    "",
    // 0x20dd: ⃝ => 
    "",
    // 0x20de: ⃞ => 
    "",
    // 0x20df: ⃟ => 
    "",
    // 0x20e0: ⃠ => 
    "",
    // 0x20e1: ⃡ => 
    "",
    // 0x20e2: ⃢ => 
    "",
    // 0x20e3: ⃣ => 
    "",
    // 0x20e4: ⃤ => [?]
    "\u005b\u003f\u005d",
    // 0x20e5: ⃥ => [?]
    "\u005b\u003f\u005d",
    // 0x20e6: ⃦ => [?]
    "\u005b\u003f\u005d",
    // 0x20e7: ⃧ => [?]
    "\u005b\u003f\u005d",
    // 0x20e8: ⃨ => [?]
    "\u005b\u003f\u005d",
    // 0x20e9: ⃩ => [?]
    "\u005b\u003f\u005d",
    // 0x20ea: ⃪ => [?]
    "\u005b\u003f\u005d",
    // 0x20eb: ⃫ => [?]
    "\u005b\u003f\u005d",
    // 0x20ec: ⃬ => [?]
    "\u005b\u003f\u005d",
    // 0x20ed: ⃭ => [?]
    "\u005b\u003f\u005d",
    // 0x20ee: ⃮ => [?]
    "\u005b\u003f\u005d",
    // 0x20ef: ⃯ => [?]
    "\u005b\u003f\u005d",
    // 0x20f0: ⃰ => [?]
    "\u005b\u003f\u005d",
    // 0x20f1: ⃱ => [?]
    "\u005b\u003f\u005d",
    // 0x20f2: ⃲ => [?]
    "\u005b\u003f\u005d",
    // 0x20f3: ⃳ => [?]
    "\u005b\u003f\u005d",
    // 0x20f4: ⃴ => [?]
    "\u005b\u003f\u005d",
    // 0x20f5: ⃵ => [?]
    "\u005b\u003f\u005d",
    // 0x20f6: ⃶ => [?]
    "\u005b\u003f\u005d",
    // 0x20f7: ⃷ => [?]
    "\u005b\u003f\u005d",
    // 0x20f8: ⃸ => [?]
    "\u005b\u003f\u005d",
    // 0x20f9: ⃹ => [?]
    "\u005b\u003f\u005d",
    // 0x20fa: ⃺ => [?]
    "\u005b\u003f\u005d",
    // 0x20fb: ⃻ => [?]
    "\u005b\u003f\u005d",
    // 0x20fc: ⃼ => [?]
    "\u005b\u003f\u005d",
    // 0x20fd: ⃽ => [?]
    "\u005b\u003f\u005d",
    // 0x20fe: ⃾ => [?]
    "\u005b\u003f\u005d",
    // 0x20ff: ⃿ => [?]
    "\u005b\u003f\u005d",
)