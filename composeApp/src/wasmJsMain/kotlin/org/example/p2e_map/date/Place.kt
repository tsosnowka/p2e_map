package org.example.p2e_map.date

class Place(
    val x: Double,
    val y: Double,
    val description: String,
    val id: Int
) {

    companion object {
        var allPlaces = getAll()
        //source of positions of the pins 1920x1080
        fun getAll(width: Int=1920, height: Int=1080) = listOf(
            Place(299.0 / width, 96.0 / height, "Kartograf Gorgias", 1),
            Place(430.0 / width, 198.0 / height, "Stocznia", 2),
            Place(483.0 / width, 198.0 / height, "Kuźnia Olberga", 3),
            Place(440.0 / width, 328.0 / height, "Port Handlowy", 4),
            Place(618.0 / width, 176.0 / height, "Karczma: Łeb Hydry", 5),
            Place(727.0 / width, 288.0 / height, "Karczma: Pod Piracką Zęzą", 6),
            Place(799.0 / width, 369.0 / height, "Magazyny Gildii", 7),
            Place(820.0 / width, 422.0 / height, "Kuźnia Jorbaga", 8),
            Place(821.0 / width, 461.0 / height, "Płatnerz Omari", 9),
            Place(886.0 / width, 441.0 / height, "I Bank Oklyoński", 10),
            Place(971.0 / width, 434.0 / height, "Główne koszary miejskie", 11),
            Place(869.0 / width, 515.0 / height, "Łuczarz Pylades", 12),
            Place(792.0 / width, 627.0 / height, "Pyrravyn - Alchemik", 13),
            Place(815.0 / width, 824.0 / height, "Koszary Południowe", 14),
            Place(850.0 / width, 1009.0 / height, "Arena Ostrakońska", 15),
            Place(738.0 / width, 825.0 / height, "Gospoda: Pod Łabędzim Skrzydłem", 16),
            Place(685.0 / width, 678.0 / height, "Świątynia Gotrel", 17),
            Place(608.0 / width, 727.0 / height, "Plac Targowy", 18),
            Place(660.0 / width, 544.0 / height, "Dykterion Meiry", 19),
            Place(1012.0 / width, 634.0 / height, "Świątynia Horkosa", 20),
            Place(980.0 / width, 727.0 / height, "Karczma: Pod Urwanym Żaglem", 21),
            Place(1064.0 / width, 126.0 / height, "Port Wojenny", 22),
            Place(1188.0 / width, 64.0 / height, "Twierdza Morska", 23),
            Place(1063.0 / width, 409.0 / height, "Beleuterion", 24),
            Place(1122.0 / width, 415.0 / height, "Trastigonejon miejski", 25),
            Place(1092.0 / width, 602.0 / height, "Biblioteka Oklyońska", 26),
            Place(1106.0 / width, 585.0 / height, "Nowa Akademia Oklyońska", 27),
            Place(1169.0 / width, 708.0 / height, "Dzielnica Arystokratów", 28),
            Place(1135.0 / width, 825.0 / height, "Handlarz Wierzchowców", 29),
            Place(1224.0 / width, 827.0 / height, "Gospoda: Spokojne Wody", 30),
            Place(1483.0 / width, 969.0 / height, "Hipodrom", 31),
            Place(1862.0 / width, 926.0 / height, "Nekropolia", 32),
            Place(1638.0 / width, 851.0 / height, "Koszary Zachodnie", 33),
            Place(1631.0 / width, 741.0 / height, "Gospoda: Sen Livo", 34),
            Place(1623.0 / width, 712.0 / height, "Świątynia Livo", 35),
            Place(1720.0 / width, 567.0 / height, "Pozostałości po klątwie", 36),
            Place(1429.0 / width, 779.0 / height, "Sklep - Biblioteka Mnemosyne", 37),
            Place(1458.0 / width, 662.0 / height, "Dykterion Arasny", 38),
            Place(1428.0 / width, 608.0 / height, "Dikasterion", 39),
            Place(1400.0 / width, 573.0 / height, "Agora", 40),
            Place(1340.0 / width, 482.0 / height, "Arena Miejska", 41),
            Place(1387.0 / width, 434.0 / height, "Herozjon Iseasa", 42),
            Place(1486.0 / width, 484.0 / height, "Łaźnie publiczne", 43),
            Place(1251.0 / width, 631.0 / height, "Ruiny teatru", 44),
            Place(1181.0 / width, 531.0 / height, "Świątynia Trastigosa Świetlistego", 45),
            Place(1263.0 / width, 338.0 / height, "Twierdza Miejska/Skarbiec", 46)
        ).sortedWith(
            compareBy(String.CASE_INSENSITIVE_ORDER) { it.description }
        ).mapIndexed { index, places ->
            Place(
                x = places.x,
                y = places.y,
                description = places.description,
                id = 1 + index
            )
        }
    }
}

